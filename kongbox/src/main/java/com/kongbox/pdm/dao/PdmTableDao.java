package com.kongbox.pdm.dao;

import com.kongbox.pdm.entity.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/24.15:37 首次创建
 * @date 2019/1/24.15:37 最后修改
 * @copyright 中科软科技股份有限公司
 */
@Service
public class PdmTableDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Column> getTableColumn(String tableName,List<String> pklist) {
        List<Column> columnList = new ArrayList<>();
        String columnSQl = "select * from user_tab_cols where table_name='" + tableName + "'";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(columnSQl);

        while (rowSet.next()) {
            Column column = new Column();
            column.setName("");
            column.setCode(rowSet.getNString(2));
            column.setType(rowSet.getNString(3));
            column.setLength(Integer.valueOf(rowSet.getNString(6)));
            column.setPrecision(Integer.valueOf(rowSet.getString(7)));
            column.setScale(Integer.valueOf(rowSet.getNString(8)));
            column.setNullable(rowSet.getString(9));
            column.setComment(rowSet.getNString(10));
            if (pklist.contains(rowSet.getString(2))) {
                column.setPK(true);
            } else {
                column.setPK(false);
            }
        }

        return columnList;
    }


    public List<String> getTablePkSet(String tableName){
        List<String> pklist =new ArrayList<>();
        String PKSQl = "select * from user_cons_columns where table_name='"+tableName+"' and constraint_name=(select   constraint_name   from   user_constraints   where   constraint_type='P' and  TABLE_name=upper('"+tableName+"'))";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(PKSQl);
        while (rowSet.next()){
            pklist.add(rowSet.getString(4));
        }
        return pklist;
    }
}
