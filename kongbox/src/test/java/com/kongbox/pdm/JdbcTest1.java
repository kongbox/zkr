package com.kongbox.pdm;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/24.10:25 首次创建
 * @date 2019/1/24.10:25 最后修改
 * @copyright 中科软科技股份有限公司
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JdbcTest1 {


    @Autowired
    DataSourceProperties dataSourceProperties;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
        // 获取配置的数据源
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        // 查看配置数据源信息
        System.out.println(dataSource);
        System.out.println(dataSource.getClass().getName());
        System.out.println(dataSourceProperties);
        //执行SQL,输出查到的数据
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String,Object>> resultList = jdbcTemplate.queryForList("SELECT * FROM LDCODE ");
        saveExecel(resultList);


        System.out.println("===>>>>>>>>>>>" + resultList.size());
    }


    public void saveExecel(List<Map<String,Object>> resultList){
        //一个表
        Workbook wb = new HSSFWorkbook();
        //一个sheet
        Sheet sheet = wb.createSheet("a");
        for (int i = 0; i < resultList.size(); i++) {
            //一行
            Row row = sheet.createRow(i);
            Map<String,Object> map =resultList.get(i);
            //一列
            createCell(wb,row,0,"LDCODE");
            createCell(wb,row,1,"LDCODE");
            createCell(wb,row,2,String.valueOf(map.get("CODETYPE")));
            createCell(wb,row,3,String.valueOf(map.get("CODE")));
            createCell(wb,row,4,String.valueOf(map.get("CODENAME")));
            
        }
        char a[]="".toCharArray();

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("C:\\Users\\kong\\Desktop\\工作簿1.xls");
            wb.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert fileOut != null;
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    private  void createCell(Workbook wb,  Row row, int column,String value) {
        Cell cell = row.createCell(column);  // 创建单元格
        cell.setCellValue(new HSSFRichTextString(value));  // 设置值
    }
}