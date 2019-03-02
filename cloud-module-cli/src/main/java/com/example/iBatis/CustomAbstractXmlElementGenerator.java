package com.example.iBatis;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * 生成公用的mapper.xml SQL
 * @Author: bingco
 * @Version: v1.0
 * @Description:
 * @Created: 2018年12月19日 16:11
 * @Package: adm-workspace - com.example
 * <p>
 * -------------------------------------------------------------------
 */
public class CustomAbstractXmlElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        // 公用select
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append("SELECT ");
        sb.append("<include refid=\"Base_Column_List\" />\n\t");
        sb.append("FROM ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        TextElement selectText = new TextElement(sb.toString());

        // 公用include
        XmlElement include = new XmlElement("include");
        include.addAttribute(new Attribute("refid", "base_query"));

        // 增加checkOrElse
        XmlElement checkOrElse = new XmlElement("update");
        checkOrElse.addAttribute(new Attribute("id", "checkOrElse"));
        sb.setLength(0);
        sb.append("UPDATE ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        sb.append(" SET status = #{status}, cfmuser = #{cfmuser}, cfmdate = NOW() WHERE id = #{id}");
        TextElement update = new TextElement(sb.toString());
        checkOrElse.addElement(update);
        parentElement.addElement(checkOrElse);

        // 增加enabledOrElse
        XmlElement enabledOrElse = new XmlElement("update");
        enabledOrElse.addAttribute(new Attribute("id", "enabledOrElse"));
        sb.setLength(0);
        sb.append("UPDATE ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        sb.append(" SET enabled = #{enabled} WHERE id = #{id}");
        update = new TextElement(sb.toString());
        enabledOrElse.addElement(update);
        parentElement.addElement(enabledOrElse);


        // 增加selectByColumn
        XmlElement selectByColumn = new XmlElement("select");
        selectByColumn.addAttribute(new Attribute("id", "selectByColumn1"));
        selectByColumn.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectByColumn.addElement(selectText);
        sb.setLength(0);
        sb.append("WHERE ");
        sb.append(getColumnPreposition(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("`${cloumn}` = #{value}");
        TextElement where = new TextElement(sb.toString());
        selectByColumn.addElement(where);
        parentElement.addElement(selectByColumn);


        // 增加selectByColumns
        XmlElement selectByColumns = new XmlElement("select");
        selectByColumns.addAttribute(new Attribute("id", "selectByColumn2"));
        selectByColumns.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectByColumns.addElement(selectText);
        sb.setLength(0);
        sb.append("WHERE ");
        sb.append(getColumnPreposition(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("`${cloumn1}` = #{value1}");
        sb.append(" AND ");
        sb.append(getColumnPreposition(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("`${cloumn2}` = #{value2}");
        where = new TextElement(sb.toString());
        selectByColumns.addElement(where);
        parentElement.addElement(selectByColumns);

        selectByColumns = new XmlElement("select");
        selectByColumns.addAttribute(new Attribute("id", "selectByColumn3"));
        selectByColumns.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectByColumns.addElement(selectText);
        sb.setLength(0);
        sb.append("WHERE ");
        sb.append(getColumnPreposition(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("`${cloumn1}` = #{value1}");
        sb.append(" AND ");
        sb.append(getColumnPreposition(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("`${cloumn2}` = #{value2}");
        sb.append(" AND ");
        sb.append(getColumnPreposition(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("`${cloumn3}` = #{value3}");
        where = new TextElement(sb.toString());
        selectByColumns.addElement(where);
        parentElement.addElement(selectByColumns);

        selectByColumns = new XmlElement("select");
        selectByColumns.addAttribute(new Attribute("id", "selectByColumn4"));
        selectByColumns.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectByColumns.addElement(selectText);
        sb.setLength(0);
        sb.append("WHERE ");
        sb.append(getColumnPreposition(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("`${cloumn1}` = #{value1}");
        sb.append(" AND ");
        sb.append(getColumnPreposition(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("`${cloumn2}` = #{value2}");
        sb.append(" AND ");
        sb.append(getColumnPreposition(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("`${cloumn3}` = #{value3}");
        where = new TextElement(sb.toString());
        sb.append(" AND ");
        sb.append(getColumnPreposition(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("`${cloumn4}` = #{value4}");
        where = new TextElement(sb.toString());
        selectByColumns.addElement(where);
        parentElement.addElement(selectByColumns);
    }

    /**
     * 获取表别名前缀：[tableAlias.]
     */
    private String getColumnPreposition(String aliasedFullyQualifiedTableName) {
        if (aliasedFullyQualifiedTableName == null || aliasedFullyQualifiedTableName.trim().isEmpty()) {
            return "";
        }
        String[] split = aliasedFullyQualifiedTableName.split("\\s+");
        if (split.length == 2) {
            return split[1] + ".";
        } else return "";
    }
}
