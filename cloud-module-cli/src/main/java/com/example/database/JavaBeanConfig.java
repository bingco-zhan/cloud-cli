package com.example.database;

import com.example.constants.ModuleConstants;
import com.example.ftl.FreemarkerUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaBeanConfig {

    private JavaBeanConfig() {}

    public static void builder() {
        JavaBeanConfig config = new JavaBeanConfig();
        try {
            config.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        try {
            File voFile = new File(ModuleConstants.classpath + "/" + ModuleConstants.basePackage.replaceAll("\\.", "/") + "/vo");
            if (!voFile.exists()) {
                voFile.mkdirs();
            }
            Template template = FreemarkerUtil.getTemplate("base/vo/templateVo.tmp");
            List<Table> tableList = DatabaseConfig.getTableList();
            for (Table table: tableList) {
                HashMap<String, Object> params = new HashMap<>();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        new File(voFile, table.getDomain().concat("Vo.java")))));
                params.put("base_package", ModuleConstants.basePackage);
                params.put("table", table);
                template.process(params, writer);
                writer.close();

                createdProvider(table);
            }

            dtoBeanBuildup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dto类构建：日期格式化,注解标注,继承
     */
    private void dtoBeanBuildup() throws IOException {
        File dtoDir = new File(ModuleConstants.classpath + "/" + ModuleConstants.basePackage.replaceAll("\\.", "/"), "/dto");
        File[] files = dtoDir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                Table table = getTableAboutMethodDtoBeanBuildup(file.getName());
                StringBuilder sb = new StringBuilder();
                String tmp;
                Boolean importUserVo = false;

                while ((tmp = reader.readLine()) != null) {
                    if (tmp.contains("private")) {
                        Column column = getColumnAboutMethodDtoBeanBuildup(table.getColumns(), tmp);
                        if (column != null) {
                            String _domain = column.getDomain().substring(0, 1).toLowerCase().concat(column.getDomain().substring(1));
                            if (!column.getComment().trim().isEmpty() && tmp.contains(_domain)) {
                                sb.append("\t@ApiModelProperty(value = \"").append(column.getComment()).append("\", example = \"\")").append("\r\n");
                            }
                            if (column.getName().contains("creuser") || column.getName().contains("moduser") || column.getName().contains("cfmuser")) {
                                importUserVo = true;
                            }
                        }
                        if (tmp.contains("Date")) {
                            sb.append("\t@JsonFormat(pattern = \"yyyy-MM-dd\", timezone = \"GMT+08:00\")").append("\r\n");
                        }
                    }
                    sb.append(tmp).append("\r\n");
                }
                reader.close();

                String importStr = "package com.by.ams.administration.dto;\r\n\r\n" +
                        "import io.swagger.annotations.ApiModelProperty;\r\n" +
                        "import io.swagger.annotations.ApiModel;";
                if (importUserVo) {
                    importStr += "\r\nimport com.by.base.mapper.UserInfo;";
                }
                if (sb.toString().contains("Date")) {
                    importStr += "\r\nimport com.fasterxml.jackson.annotation.JsonFormat;";
                }
                tmp = sb.toString().replace("package com.by.ams.administration.dto;", importStr);
                if (importUserVo) {
                    Pattern compile = Pattern.compile("public class [^\\{]+ \\{");
                    Matcher matcher = compile.matcher(tmp);
                    matcher.find();
                    String clazzStr = matcher.group();
                    tmp = tmp.replaceAll("public class [^\\{]+ \\{", clazzStr.substring(0, clazzStr.length() - 2).concat(" extends UserInfo {"));
                }
                tmp = tmp.replace("public class", "@ApiModel(\"" + table.getComment().replace("表", "") + "数据模版\")\r\npublic class");
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(tmp);
                writer.close();
            }
        }
    }

    private Column getColumnAboutMethodDtoBeanBuildup(List<Column> columns, String lineStr) {
        if (columns == null || columns.size() <= 0) return null;

        for (Column column : columns) {
            if (lineStr.endsWith(column.getDomain().concat(";"))) {
                return column;
            }
        }
        return null;
    }

    private Table getTableAboutMethodDtoBeanBuildup(String filename) {
        List<Table> tableList = DatabaseConfig.getTableList();
        if (tableList == null || tableList.size() <= 0) return null;

        for (Table table : tableList) {
            if (filename.equals(table.getDomain() + ".java")) {
                return table;
            }
        }
        return null;
    }

    /**
     * 创建Provider.java文件
     * @param table
     */
    private void createdProvider(Table table) throws IOException {
        String provider = table.getDomain().concat("Provider");
        List<String> username = new ArrayList<>();
        List<String> baseJoin = new ArrayList<>();
        List<Column> columns = table.getColumns();
        for (Column column : columns) {
            if (column.getDomain().equalsIgnoreCase("creuser")) {
                username.add("creuser.username AS creuserName");
                baseJoin.add(" LEFT JOIN `ams-basic`.`secuser` creuser ON creuser.id = t.creuser ");
            } else if (column.getDomain().equalsIgnoreCase("moduser")) {
                username.add("moduser.username AS moduserName");
                baseJoin.add(" LEFT JOIN `ams-basic`.`secuser` moduser ON moduser.id = t.moduser ");
            } else if (column.getDomain().equalsIgnoreCase("cfmuser")) {
                username.add("cfmuser.username AS cfmuserName");
                baseJoin.add(" LEFT JOIN `ams-basic`.`secuser` cfmuser ON cfmuser.id = t.cfmuser ");
            }
        }

        File voFile = new File(ModuleConstants.classpath + "/" + ModuleConstants.basePackage.replaceAll("\\.", "/") + "/mapper/provider");
        if (!voFile.exists()) {
            voFile.mkdirs();
        }
        Template template = FreemarkerUtil.getTemplate("base/mapper/TemplateProvider.tmp");
        try (Writer writer =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                new File(voFile, table.getDomain().concat("Provider.java")))))) {
            Map<String, Object> params = new HashMap<>();
            params.put("base_package", ModuleConstants.basePackage);
            params.put("provider", provider);
            params.put("table", table);
            params.put("username", username);
            params.put("baseJoin", baseJoin);
            template.process(params, writer);
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
