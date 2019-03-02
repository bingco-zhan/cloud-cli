package com.example.iBatis;

import com.example.constants.ModuleConstants;
import com.example.database.DatabaseConfig;
import com.example.database.Table;
import com.example.ftl.FreemarkerUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import freemarker.template.Template;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IBatis初始化与配置
 *
 * @Author: bingco
 * @Version: v1.0
 * @Description:
 * @Created: 2018年12月31日 12:01
 * @Package: adm-workspace - com.example.iBatis
 * <p>
 * -------------------------------------------------------------------
 */
public class IBatisConfig {

    private IBatisConfig() { }

    public static void builder() {
        IBatisConfig config = new IBatisConfig();
        try {
            config.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        BufferedWriter writer = null;
        try {
            List<Table> tableList = DatabaseConfig.getTableList();
            if (tableList != null && tableList.size() > 0) {
                Template template = FreemarkerUtil.getTemplate("iBatis/generatorConfig.tmp");
                Map<String, Object> model = new HashMap<>();
                model.put("tables", tableList);
                model.put("mysql_ip", ModuleConstants.ip);
                model.put("database_name", ModuleConstants.database);
                model.put("mysql_user", ModuleConstants.name);
                model.put("mysql_password", ModuleConstants.password);
                model.put("base_package", ModuleConstants.basePackage);
                model.put("classpath", ModuleConstants.classpath);
                model.put("resources", ModuleConstants.resources);
                File file = new File(getResourcePath(), "/generatorConfig.xml");
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                template.process(model, writer);

                System.out.println("\t-- generate begin --");
                List<String> warnings = new ArrayList<>();
                ConfigurationParser cp = new ConfigurationParser(warnings);
                Configuration configuration = cp.parseConfiguration(file);
                DefaultShellCallback callback = new DefaultShellCallback(true);
                MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, callback, warnings);
                myBatisGenerator.generate(null);
                System.out.println("\t-- generate over --");

                File mapperDir = new File(ModuleConstants.classpath, ModuleConstants.basePackage.replaceAll("[.]", "/").concat("/mapper/"));
                File[] mapperDtoFiles = mapperDir.listFiles();
                if (mapperDtoFiles != null && mapperDtoFiles.length > 0) {
                    for (File fe : mapperDtoFiles) {
                        if (fe.isFile()) reformMapperDto(fe);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

    String dto_regex = "\\S\\w+Mapper";

    /**
     * 改造 MapperDto 接口
     * @param file
     */
    private void reformMapperDto(File file) {
        String tmp = "";

        try (BufferedReader _reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            StringBuilder sb = new StringBuilder();

            while ((tmp = _reader.readLine()) != null) {
                sb.append(tmp).append("\r\n");
            }
            Pattern pattern = Pattern.compile(dto_regex);
            Matcher matcher = pattern.matcher(sb.toString());
            matcher.find();
            String dto = matcher.group().replace("Mapper", "");
            tmp = sb.toString().replaceAll(" \\{([^\\}]+)\\}", " extends IMapper<" +
                    dto + "Vo, " + dto + "> \\{\r\n\t@SelectProvider(type = " + dto + "Provider.class, method = \"selectSelective\")" +
                    "\r\n\tList<" + dto + "> selectSelective(" + dto + "Vo record);" + "\r\n\\}");

            tmp = tmp.replaceAll("import org.apache.ibatis.annotations.Param;",
                            "import com.by.base.mapper.IMapper;\r\n" +
                            "import org.apache.ibatis.annotations.SelectProvider;\r\n" +
                            "import " + ModuleConstants.basePackage + ".vo." + dto + "Vo;\r\n" +
                            "import " + ModuleConstants.basePackage + ".mapper.provider." + dto + "Provider;\r\n" +
                            "import org.springframework.stereotype.Component;\r\n\r\n" +

                                    "@Component");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            writer.write(tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getResourcePath() {
        return new File(IBatisConfig.class.getResource("/").getFile().replace("/target/classes", ""), "\\src\\main\\resources\\").getPath();
    }
}
