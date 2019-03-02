package com.example.service;

import com.example.constants.ModuleConstants;
import com.example.database.DatabaseConfig;
import com.example.database.Table;
import com.example.ftl.FreemarkerUtil;
import freemarker.template.Template;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;

/**
 * 加载service层的模版构建
 *
 * @Author: bingco
 * @Version: v1.0
 * @Description:
 * @Created: 2019年01月04日 18:27
 * @Package: adm-workspace - com.example.service
 * <p>
 * -------------------------------------------------------------------
 */
public class ServiceConfig {

    public static void builder() {
        ServiceConfig config = new ServiceConfig();
        config.init();
    }

    private void init() {
        try {
            File serviceDir = new File(ModuleConstants.classpath + "/" + ModuleConstants.basePackage.replaceAll("\\.", "/") + "/service");
            if (!serviceDir.exists()) {
                serviceDir.mkdir();
            }
            File serviceImlDir = new File(serviceDir, "/impl");
            if (!serviceImlDir.exists()) {
                serviceImlDir.mkdir();
            }

            Template template1 = FreemarkerUtil.getTemplate("base/service/TemplateService.tmp");
            Template template2 = FreemarkerUtil.getTemplate("base/service/TemplateServiceImpl.tmp");

            List<Table> tableList = DatabaseConfig.getTableList();
            for (Table table: tableList) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("base_package", ModuleConstants.basePackage);
                params.put("table", table);

                BufferedWriter writer1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        new File(serviceDir, table.getDomain().concat("Service.java")))));
                template1.process(params, writer1);
                writer1.close();

                BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                        new File(serviceImlDir, table.getDomain().concat("ServiceImpl.java")))));
                template2.process(params, writer2);
                writer2.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
