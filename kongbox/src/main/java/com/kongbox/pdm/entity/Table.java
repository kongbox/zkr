package com.kongbox.pdm.entity;

import java.util.List;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/22.10:09 首次创建
 * @date 2019/1/22.10:09 最后修改
 * @copyright 中科软科技股份有限公司
 */
public class Table {
    private String tableId;
    private String tableName;
    private String tableCode;
    private String parentTableId;
    private String comment;
    private List<Column> allColumns;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getParentTableId() {
        return parentTableId;
    }

    public void setParentTableId(String parentTableId) {
        this.parentTableId = parentTableId;
    }

    public List<Column> getAllColumns() {
        return allColumns;
    }

    public void setAllColumns(List<Column> allColumns) {
        this.allColumns = allColumns;
    }


}
