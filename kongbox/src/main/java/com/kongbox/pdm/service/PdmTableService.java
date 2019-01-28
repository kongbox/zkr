package com.kongbox.pdm.service;

import com.kongbox.pdm.dao.PdmTableDao;
import com.kongbox.pdm.entity.Column;
import com.kongbox.pdm.entity.Table;
import com.kongbox.pdm.util.PdmUtli;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/24.15:31 首次创建
 * @date 2019/1/24.15:31 最后修改
 * @copyright 中科软科技股份有限公司
 */
@Service
public class PdmTableService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PdmTableDao pdmTableDao;


    /**
     *
     *
     * @return
     */
    public Workbook pdmTable(String filePath){
        List<Table> tableList =new ArrayList<>();
        List<Table> tableListPdm =new ArrayList<>();
        List<Table> tableList1 =new ArrayList<>();
        /*
        读取pdm
        获取数据库表信息
        获取pdm表信息
         */
        Document doc = PdmUtli.readDocByFile(filePath);
        for (Node node:doc.selectNodes("//c:Tables//o:Table")){
            Element eTable = (Element) node;
            Table tablePdm =new Table();
            tablePdm.setTableName(eTable.elementTextTrim("Name"));
            tablePdm.setTableCode(eTable.elementTextTrim("Name"));
            tablePdm.setComment(eTable.elementTextTrim("Comment"));
            //获取表主键
            List<String> pkList =pdmTableDao.getTablePkSet(tablePdm.getTableName());
            if (pkList.isEmpty()){
                System.out.println("数据库中没有这张表【"+tablePdm.getTableName()+":"+tablePdm.getTableCode()+"】");
                continue;
            }else {
                //数据库列
                List<Column> columnList =pdmTableDao.getTableColumn(tablePdm.getTableName(),pkList);
                Table table =new Table();
                table.setTableCode(eTable.elementTextTrim("Name"));
                table.setAllColumns(columnList);
                tableList.add(table);
            }
            List<Column> columnList =PdmUtli.getColumnListByDoc(eTable,pkList);
            tablePdm.setAllColumns(columnList);
            tableListPdm.add(tablePdm);
        }
        Workbook workbook=null;
        try {
             workbook = PdmUtli.saveExecl(tableListPdm);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return workbook;
    }


}
