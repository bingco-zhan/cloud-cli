package com.example.ftl;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;

/**
 * 模版引擎工具类
 * @Author: bingco
 * @Version: v1.0
 * @Description:
 * @Created: 2018年12月31日 13:33
 * @Package: adm-workspace - com.example.ftl
 * <p>
 * -------------------------------------------------------------------
 */
public class FreemarkerUtil {

    private static Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    static {
        configuration.setClassForTemplateLoading(FreemarkerUtil.class, "/tmp");
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static Template getTemplate(String name) throws IOException {
        return configuration.getTemplate(name);
    }
}
