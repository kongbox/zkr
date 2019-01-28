package com.kongbox.pdm.service;

import com.kongbox.pdm.entity.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

/**
 * Created by 10412 on 2016/12/27.
 * JDBC的六大步骤
 * JAVA连接Oracle的三种方式
 */
@Service
public class  JdbcTest {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Column> getTableList(String tableName){
        SqlRowSet tableList = jdbcTemplate.queryForRowSet("select * from user_tab_cols where table_name='" + tableName + "'");

        String PKSQl = "select * from user_cons_columns where table_name='"+tableName+"' and constraint_name=(select   constraint_name   from   user_constraints   where   constraint_type='P' and  TABLE_name=upper('"+tableName+"'))";
        SqlRowSet oracleTableList = jdbcTemplate.queryForRowSet(PKSQl);
        Set<String> pkset=new HashSet<>();
        while (oracleTableList.next()){
            pkset.add(oracleTableList.getString(4));
        }

        return null;
    }


    public static void main(String[] args) {
        getData("LLCASE");
    }

    public static List<Column> getData(String table){
        List<Column> dataResult =new ArrayList<>();
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        ResultSet columns = null;
        ResultSet resultSetPK = null;
        table =table.toUpperCase();
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            String url = "jdbc:oracle:thin:@10.100.54.32:1521:orcl";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
            String user = "lis";// 用户名,系统默认的账户名
            String password = "lis";// 你安装时选设置的密码
            con = DriverManager.getConnection(url, user, password);// 获取连接
            String columnSQl = "select * from user_tab_cols where table_name='"+table+"'";
            pre = con.prepareStatement(columnSQl);
            columns = pre.executeQuery();
            String PKSQl = "select * from user_cons_columns where table_name='"+table+"' and constraint_name=(select   constraint_name   from   user_constraints   where   constraint_type='P' and  TABLE_name=upper('"+table+"'))";
            PreparedStatement statementPKSQl =con.prepareStatement(PKSQl);
            resultSetPK =statementPKSQl.executeQuery();
            Set<String> pkset=new HashSet<>();
            while (resultSetPK.next()){
                pkset.add(resultSetPK.getString(4));
            }

            while (columns.next()) {
                Column column =new Column();
                String row[]=new String[10];


                //表名
                row[0]=columns.getString(1);
                //列名
                column.setCode(columns.getString(2));
                row[1]=columns.getString(2);
                //data_type
                column.setType(columns.getString(3));
                row[2]=columns.getString(3);
                //data_length
                column.setLength(Integer.valueOf(columns.getString(6)));
                row[3]=columns.getString(6);
                //data_precisio
                if (columns.getString(7)!=null) {
                    column.setPrecision(Integer.valueOf(columns.getString(7)));
                    row[4]=columns.getString(7);
                }
                //data_scale
                if (columns.getString(8)!=null){
                    column.setScale(Integer.valueOf(columns.getString(8)));
                    row[5]=columns.getString(8);
                }
                //nullable
                column.setNullable(columns.getString(9));
                row[6]=columns.getString(9);
                //pk
                if(pkset.contains(columns.getString(2))){
                    column.setPK(true);
                    row[7]="true";
                }else {
                    column.setPK(false);
                    row[7]="false";
                }
                dataResult.add(column);
            }
            //
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
                if (resultSetPK != null) {
                    resultSetPK.close();
                }
                if (columns != null) {
                    columns.close();
                }
                if (pre != null) {
                    pre.close();
                }
                if (con != null) {
                    con.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return dataResult;
    }
}
