package com.kongbox.pdm.service;


import com.kongbox.pdm.entity.Column;
import com.kongbox.pdm.entity.Table;
import com.kongbox.pdm.util.PdmUtli;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/22.10:05 首次创建
 * @date 2019/1/22.10:05 最后修改
 * @copyright 中科软科技股份有限公司
 */
@Service
public class PdmParser {


    public List<Table> parsePDM_VO(String filePath) {
        List<Table> tables = new ArrayList<Table>();
        File f = new File(filePath);
        Document doc = null;
        try {
            SAXReader sr = new SAXReader();
            doc = sr.read(f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //主键
        assert doc != null;
        for (Object o1 : doc.selectNodes("//c:Tables//o:Table")) {
            Table pdmTable = new Table();
            List<Column> list = new ArrayList<Column>();
            Column col;
            Element eTable = (Element) o1;
            pdmTable.setTableName(eTable.elementTextTrim("Name"));
            pdmTable.setTableCode(eTable.elementTextTrim("Code"));
            //jdbc table
            List<Column> jdbcTable= JdbcTest.getData(pdmTable.getTableCode());
            //表对比
            if (jdbcTable==null||jdbcTable.isEmpty()){
                System.out.println("数据库中没有这张表：【"+pdmTable.getTableName()+":"+pdmTable.getTableCode()+"】");
                continue;
            }else {
                System.out.println("存在表：【"+pdmTable.getTableName()+":"+pdmTable.getTableCode()+"】");
            }
            //字段对比
            List<String>  allPKIds = PdmUtli.eTableKeys(eTable);
            for (Object o : eTable.element("Columns").elements("Column")) {
                try {
                    col = new Column();
                    Element eCol = (Element) o;
                    String pkID = eCol.attributeValue("Id");
                    //详情
                    col.setComment(eCol.elementTextTrim("Comment"));
                    //默认值
                    col.setDefaultValue(eCol.elementTextTrim("DefaultValue"));
                    //字段中文名
                    col.setName(eCol.elementTextTrim("Name"));
                    //类型
                    if (eCol.elementTextTrim("DataType").indexOf("(") > 0) {
                        col.setType(eCol.elementTextTrim("DataType").substring(0, eCol.elementTextTrim("DataType").indexOf("(")));
                    } else {
                        col.setType(eCol.elementTextTrim("DataType"));
                    }
                    //字段英文名
                    col.setCode(eCol.elementTextTrim("Code"));
                    //空
                    col.setNullable(eCol.elementTextTrim("Mandatory")!=null?"N":"Y");
                    //字段长度
                    col.setLength(eCol.elementTextTrim("Length") == null ? null : Integer.parseInt(eCol.elementTextTrim("Length")));
                    //小数，比例
                    if (eCol.elementTextTrim("Precision")!=null){
                        col.setScale(Integer.valueOf(eCol.elementTextTrim("Precision")));
                    }
                    //主键
                    if (allPKIds.contains(pkID)) {
                       col.setPK(true);
                        col.setNullable("N");
                    }

                    /*
                    对比jdbc
                    */
                    col =PdmUtli.conparaColumn(col,jdbcTable);
                    list.add(col);
                } catch (Exception ex) {
                    System.out.println("+++++++++有错误+++++++++++++");
                    ex.printStackTrace();
                }
            }
            pdmTable.setAllColumns(list);
            tables.add(pdmTable);
        }
        return tables;
    }

    public static void main(String[] args) throws IOException {
        PdmParser pp = new PdmParser();
        List<Table> tables = pp.parsePDM_VO("C:\\Users\\kong\\Desktop\\work\\个团合一数据字典\\New_PDM\\PDMS-BH\\理赔管理.pdm");

        saveExecl(tables);
    }


    /**
     * 保存execl
     *
     * @param tables
     */
    public static void saveExecl(List<Table> tables) throws IOException {
        // 定义一个新的工作簿
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("a");
        int rowNum = 0;
        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
            // 创建一个Sheet页
            String tableName = table.getTableName() + table.getTableCode();

            List<Column> columns = table.getAllColumns();
            for (int j = 0; j < columns.size(); j++) {
                Column column = columns.get(j);
                if (column!=null) {
                    // 创建一个行
                    Row row = sheet.createRow(rowNum++);
                /* execl格式
                Table Name
                Module Name
                Column Name
                Data Type
                Length
                Nullable
                Key
                Short Description (EN)
                Short Description (CN)
                Reference Table
                */

                    //Table Name    表名
                    createCell(wb, 0, row, table.getTableCode());
                    //Module Name	模块
                    createCell(wb, 1, row, "");
                    //Column Name   列名
                    createCell(wb, 2, row, column.getCode());
                    //Data Type
                    createCell(wb, 3, row, column.getType());
                    //Length
                    if (column.getLength() != null) {
                        if (column.getScale() != null){
                            if(column.getPrecision()==null){
                                createCell(wb, 4, row, column.getLength() + "," + column.getScale());
                            }else {
                                createCell(wb, 4, row, column.getPrecision() + "," + column.getScale());
                            }
                        }else {
                            createCell(wb, 4, row, column.getLength() + "");
                        }
                    }
                    //Nullable
                    createCell(wb, 5, row, Objects.equals(column.getNullable(), "N") ? "NOT NULL" : "NULL");
                    //Key
                    createCell(wb, 6, row, column.isPK() ? "PK" : "");
                    //Short Description (EN)
                    createCell(wb, 7, row, column.getCode());
                    //Short Description (CN)
                    createCell(wb, 8, row, column.getName());
                    //Reference Table
                    createCell(wb, 9, row, column.getComment());

                }
            }

        }


        FileOutputStream fileOut = new FileOutputStream("C:\\Users\\kong\\Desktop\\工作簿.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    /**
     * 创建一个单元格并为其设定指定的对其方式
     *
     * @param wb     工作簿
     * @param row    行
     * @param column 列
     */
    private static void createCell(Workbook wb, int column, Row row, String value) {
        Cell cell = row.createCell(column);  // 创建单元格
        cell.setCellValue(new HSSFRichTextString(value));  // 设置值
    }
}
