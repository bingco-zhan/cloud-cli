package com.example.database;

import java.util.List;

/**
 * 表结构javaBean
 *
 * @Author: bingco
 * @Version: v1.0
 * @Description:
 * @Created: 2018年12月31日 15:07
 * @Package: adm-workspace - com.example.database
 * <p>
 * -------------------------------------------------------------------
 */
public class Table {

    private String name;

    private String comment;

    private String domain;

    private List<Column> columns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
