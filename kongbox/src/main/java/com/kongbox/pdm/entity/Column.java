package com.kongbox.pdm.entity;

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
public class Column {
    //列id
    private String colId;
    //列名称
    private String name;
    //英文名
    private String code;
    private String domain;
    private String comment;
    private String DefaultValue;
    private String type;
    //长度
    private Integer length;
    //精度
    private Integer precision;
    //比例，小数
    private Integer scale;
    //注释
    private String annotation;
    //可为null
    private String nullable;
    //强制
    private Integer mandatory;
    private boolean FK;
    private boolean PK;

    public String getColId() {
        return colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDefaultValue() {
        return DefaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        DefaultValue = defaultValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public Integer getMandatory() {
        return mandatory;
    }

    public void setMandatory(Integer mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isFK() {
        return FK;
    }

    public void setFK(boolean FK) {
        this.FK = FK;
    }

    public boolean isPK() {
        return PK;
    }

    public void setPK(boolean PK) {
        this.PK = PK;
    }
}
