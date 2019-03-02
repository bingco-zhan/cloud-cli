package com.example.database;

import com.example.constants.ModuleConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据源信息抓取
 *
 * @Author: bingco
 * @Version: v1.0
 * @Description:
 * @Created: 2018年12月31日 15:13
 * @Package: adm-workspace - com.example.database
 * <p>
 * -------------------------------------------------------------------
 */
public class DatabaseConfig {

    private static List<Table> tableList = new ArrayList<>();

    private static final String url_regex = "jdbc:mysql://{{ip}}:3306/{{datasource}}?useUnicode=true&amp;characterEncoding=UTF-8&amp;useSSL=true";

    public static void builder() {
        try {
            Connection connection = DriverManager.getConnection(
                    url_regex.replace("{{ip}}", ModuleConstants.ip).replace("{{datasource}}", ModuleConstants.database),
                        ModuleConstants.name, ModuleConstants.password);

            String catalog = connection.getCatalog();
            DatabaseMetaData metaData = connection.getMetaData();
            String schema = metaData.getUserName().toUpperCase();
            ResultSet tables = metaData.getTables(catalog, null, null, null);
            while (tables.next()) {
                Table table = new Table();
                String name = tables.getString("TABLE_NAME");
                ResultSet resultSet = connection.getMetaData().getColumns(null, schema, name, "%");
                List<Column> columns = new ArrayList<>();
                table.setName(name);
                String domain = conversionHump(name);
                table.setDomain(domain);
                Statement statement = connection.createStatement();
                statement.execute("SHOW CREATE TABLE `" + name + "`");
                ResultSet tableResultSet = statement.getResultSet();
                tableResultSet.next();
                String createTableSql = tableResultSet.getString("Create Table");
                table.setComment(pattrenTableCommnet(createTableSql));
                while (resultSet.next()) {
                    Column column = new Column();
                    column.setName(resultSet.getString("COLUMN_NAME"));
                    String _columnDomain = conversionHump(column.getName());
                    String columnDomain = _columnDomain.substring(0, 1).toLowerCase().concat(_columnDomain.substring(1));
                    column.setDomain(columnDomain);
                    column.setGetting("get" + _columnDomain);
                    column.setSetting("set" + _columnDomain);
                    column.setComment(resultSet.getString("REMARKS"));
                    column.setType(conversionJavaType(resultSet.getString("TYPE_NAME")));
                    columns.add(column);
                }
                table.setColumns(columns);
                tableList.add(table);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static String pattrenTableCommnet(String string) {
        String regex = "COMMENT='[^']+'";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group().replace("COMMENT='", "").replace("'", "").replace("表","");
        }
        return "";
    }

    private static String conversionJavaType(String name) {
        if ("VARCHAR".equalsIgnoreCase(name) || "CHAR".equalsIgnoreCase(name)) {
            return "String";
        }

        if ("BIGINT".equalsIgnoreCase(name)) {
            return "Long";
        }

        if ("INT".equalsIgnoreCase(name)) {
            return "Integer";
        }

        if ("TINYINT".equalsIgnoreCase(name)) {
            return "Byte";
        }

        if ("DATETIME".equalsIgnoreCase(name) || "TIMESTAMP".equalsIgnoreCase(name) || "DATE".equalsIgnoreCase(name)) {
            return "java.util.Date";
        }

        if ("DECIMAL".equalsIgnoreCase(name)) {
            return "java.math.BigDecimal";
        }

        if ("SMALLINT".equalsIgnoreCase(name)) {
            return "Byte";
        }

        return "String";
    }

    /**
     * 驼峰转换
     * @param name
     * @return
     */
    private static String conversionHump(String name) {
        String domain = "";
        String[] split = name.split("_");
        for (String st : split) {
            if (st.length() > 0) {
                domain = domain.concat(st.substring(0, 1).toUpperCase().concat(st.substring(1)));
            }
        }
        return domain;
    }

    public static List<Table> getTableList() {
        return tableList;
    }
}
