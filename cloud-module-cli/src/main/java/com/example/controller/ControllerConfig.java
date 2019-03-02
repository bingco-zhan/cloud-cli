package com.example.controller;

import com.example.constants.ModuleConstants;
import com.example.database.DatabaseConfig;
import com.example.database.Table;
import com.example.ftl.FreemarkerUtil;
import freemarker.template.Template;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 加载controller层的模版构建
 *
 * @Author: bingco
 * @Version: v1.0
 * @Description:
 * @Created: 2019年01月05日 13:33
 * @Package: adm-workspace - com.example.controller
 * <p>
 * -------------------------------------------------------------------
 */
public class ControllerConfig {

    public static void builder() {
        ControllerConfig config = new ControllerConfig();
        config.init();
    }

    private void init() {
        try {
            File controllerDir = new File(ModuleConstants.classpath + "/" + ModuleConstants.basePackage.replaceAll("\\.", "/") + "/controller");
            if (!controllerDir.exists()) {
                controllerDir.mkdir();
            }

            Template template1 = FreemarkerUtil.getTemplate("base/controller/TemplateController.tmp");

            List<Table> tableList = DatabaseConfig.getTableList();
            for (Table table: tableList) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("base_package", ModuleConstants.basePackage);
                params.put("table", table);
                AtomicInteger hasCheckOrEnabled = new AtomicInteger(0);
                table.getColumns().forEach(o -> {
                    if (o.getName().contains("status")) {
                        hasCheckOrEnabled.set(2);
                    } else if (o.getName().contains("enabled")) {
                        hasCheckOrEnabled.set(1);
                    }
                });
                params.put("has_check_or_enabled", hasCheckOrEnabled.get());
                BufferedWriter writer1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        new File(controllerDir, table.getDomain().concat("Controller.java")))));
                template1.process(params, writer1);
                writer1.close();
            }

            File configDir = new File(ModuleConstants.classpath + "/" + ModuleConstants.basePackage.replaceAll("\\.", "/"), "/config");
            if (!configDir.exists()) {
                configDir.mkdir();
            }
            Template template3 = FreemarkerUtil.getTemplate("base/config/SwaggerConfig.tmp");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(configDir, "/SwaggerConfig.java"))));
            HashMap<String, Object> params = new HashMap<>();
            params.put("base_package", ModuleConstants.basePackage);
            template3.process(params, writer);
            writer.close();

            Template template4 = FreemarkerUtil.getTemplate("base/pom.xml");
            writer = new BufferedWriter(new FileWriter(new File(ModuleConstants.modulePath, "/pom.xml")));
            params.put("project_name", ModuleConstants.projectName);
            template4.process(params, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
