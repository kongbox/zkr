package com.kongbox.pdm.util;

import com.kongbox.pdm.entity.Column;
import com.kongbox.pdm.entity.Table;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/22.15:31 首次创建
 * @date 2019/1/22.15:31 最后修改
 * @copyright 中科软科技股份有限公司
 */
public class PdmUtli {

    /**
     * 加载所有主键Id
     * @param doc
     */
    public static List<String> loadAllPKIds(Document doc){
        List<String> allPKIds = new ArrayList<String>();
        Element tableNode = (Element) doc.selectSingleNode("//c:Tables");
        for(Object e:tableNode.elements()){
            //判断是否有主键
            Element keys = ((Element)e).element("Keys");
            if(keys != null){
                String PKId = keys.element("Key").element("Key.Columns").element("Column").attributeValue("Ref");
                allPKIds.add(PKId);
            }
        }
        return allPKIds;
    }

    public static List<String> eTableKeys(Element eTable){
        List<String> allPKIds = new ArrayList<String>();
        if (eTable.element("Keys") != null) {
            //主键的keys
            String keysPrimariesRefId = eTable.element("PrimaryKey").element("Key").attributeValue("Ref");

            Element keys = eTable.element("Keys");

            for (Object key:keys.elements("Key")){

                String keysId =((Element) key).attributeValue("Id");
                if (keysPrimariesRefId.equals(keysId)){
                    // 其中一个keyid
                    Element keyColumns = eTable.element("Keys").element("Key").element("Key.Columns");
                    if (keyColumns!=null) {
                        for (Object kc : keyColumns.elements("Column")) {
                            Element keyColumn = (Element) kc;
                            allPKIds.add(keyColumn.attributeValue("Ref"));
                        }
                    }else {
                        System.out.printf("没有主键——————————————————————————————————————");
                        System.out.printf(keysId);
                        System.out.printf(eTable.elementTextTrim("Name"));
                        System.out.printf(eTable.elementTextTrim("Code"));
                        allPKIds.add(keysId);
                    }

                }
            }
        }
        return allPKIds;
    }


    /**
     *
     * @param column
     * @param jdbccol
     * @return
     */
    public static Column conparaColumn(Column column, List<Column> jdbccol){
        Column columnJDBC =null;
        for (int i = 0; i <jdbccol.size() ; i++) {
            if (column.getCode().toUpperCase().equals(jdbccol.get(i).getCode().toUpperCase())){
                columnJDBC=jdbccol.get(i);
                break;
            }
        }
        if (columnJDBC==null){
            System.out.println("此列数据库中没有：【"+column.getCode()+"】");
            return null;
        }
        //类型对比
        if(!Objects.equals(column.getType(), columnJDBC.getType())){
            System.out.println("此列类型不一样：【"+column.getCode()+"】");
            column.setType(columnJDBC.getType());
        }
        //长度对比
        if (!Objects.equals(column.getLength(), columnJDBC.getLength())){
            System.out.println("此列长度不一样：【"+column.getCode()+"】");
            column.setLength(columnJDBC.getLength());
        }
        //有效位
        if (!Objects.equals(column.getPrecision(), columnJDBC.getPrecision())){
            System.out.println("此列有效位不一样：【"+column.getCode()+"】");
            column.setPrecision(columnJDBC.getPrecision());
        }
        //小数位
        if (!Objects.equals(column.getScale(), columnJDBC.getScale())){
            System.out.println("此列小数位不一样：【"+column.getCode()+"】");
            column.setScale(columnJDBC.getScale());
        }
        //非空
        if (!Objects.equals(column.getNullable(), columnJDBC.getNullable())){
            System.out.println("此列非空不一样：【"+column.getCode()+"】");
            column.setNullable(columnJDBC.getNullable());
        }
        //主键
        if (!column.isPK()==columnJDBC.isPK()){
            System.out.println("此列主键不一样：【"+column.getCode()+"】");
        }
        return column;
    }



    public static Document readDocByFile(String filePath){
        File f = new File(filePath);
        Document doc = null;
        try {
            SAXReader sr = new SAXReader();
            doc = sr.read(f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return  doc;
    }


    public static List<Column>  getColumnListByDoc(Element eTable,List<String> pkList){
        List<Column> columnList =new ArrayList<>();
        for (Element eColumn:eTable.element("Columns").elements("Column")){
            Column column =new Column();
            String pkID = eColumn.attributeValue("Id");
            //详情
            column.setComment(eColumn.elementTextTrim("Comment"));
            //默认值
            column.setDefaultValue(eColumn.elementTextTrim("DefaultValue"));
            //字段中文名
            column.setName(eColumn.elementTextTrim("Name"));
            //类型
            if (eColumn.elementTextTrim("DataType").indexOf("(") > 0) {
                column.setType(eColumn.elementTextTrim("DataType").substring(0, eColumn.elementTextTrim("DataType").indexOf("(")));
            } else {
                column.setType(eColumn.elementTextTrim("DataType"));
            }
            //字段英文名
            column.setCode(eColumn.elementTextTrim("Code"));
            //空
            column.setNullable(eColumn.elementTextTrim("Mandatory")!=null?"N":"Y");
            //字段长度
            column.setLength(eColumn.elementTextTrim("Length") == null ? null : Integer.parseInt(eColumn.elementTextTrim("Length")));
            //小数，比例
            if (eColumn.elementTextTrim("Precision")!=null){
                column.setScale(Integer.valueOf(eColumn.elementTextTrim("Precision")));
            }
            //主键
            if (pkList.contains(pkID)) {
                column.setPK(true);
                column.setNullable("N");
            }
            columnList.add(column);
        }
        return columnList;
    }
    /**
     * 保存execl
     *
     * @param tables
     */
    public static Workbook saveExecl(List<Table> tables) throws IOException {
        // 定义一个新的工作簿
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("a");
        int rowNum = 0;
        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
            // 创建一个Sheet页
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
        return wb;
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
