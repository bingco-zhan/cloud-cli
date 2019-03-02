package com.example.constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 加载基本信息录入
 * @Author: bingco
 * @Version: v1.0
 * @Description:
 * @Created: 2018年12月31日 12:30
 * @Package: adm-workspace - com.example.main
 * <p>
 * -------------------------------------------------------------------
 */
public class BannerAndAttributeConfig {

    public static final Logger log = Logger.getLogger(BannerAndAttributeConfig.class.toString());
    static {
        log.setLevel(Level.ALL);
    }

    public static void builder() {
        System.out.println("+----------------------------------------+\r\n" +
                           "|               bingco.com               |\n" +
                           "|               代码生成器 v1             |\n" +
                           "+----------------------------------------+");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
//            System.out.print("请输入数据源IP：");
//            String message = reader.readLine();
//            ModuleConstants.ip = message;
//
//            System.out.print("请输入数据库名称：");
//            message = reader.readLine();
//            ModuleConstants.database = message;
//
//            System.out.print("请输入数据库连接账户：");
//            message = reader.readLine();
//            ModuleConstants.name = message;
//
//            System.out.print("请输入数据库连接密码：");
//            message = reader.readLine();
//            ModuleConstants.password = message;
//
//            System.out.print("请输入项目生成路径：");
//            message = reader.readLine();
//            ModuleConstants.modulePath = message;
//
//            System.out.print("请输入项目生成包名：");
//            message = reader.readLine();
//            ModuleConstants.basePackage = message;

            File[] files = new File(ModuleConstants.modulePath).listFiles();
            if (files != null && files.length > 0) {
                for (File fe : files) {
                    removeFile(fe);
                }
            }

            File file = new File(ModuleConstants.modulePath, "\\src\\main\\");
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if (mkdirs) {
                    log.info("模块目录创建完成:" + file.getPath());
                }
            }

            File javaFile = new File(file, "\\java\\");
            boolean mkdirs = javaFile.mkdirs();
            if (mkdirs) {
                log.info("模块目录创建完成:" + file.getPath());
            }

            File resourcesFile = new File(file, "\\resources\\");
            mkdirs = resourcesFile.mkdirs();
            if (mkdirs) {
                log.info("模块目录创建完成:" + file.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void removeFile(File file) {
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File fe : files) {
                    removeFile(fe);
                }
            } else {
                boolean delete = file.delete();
                if (delete) {
                    log.info("清理目录:" + file);
                }
            }
        }
    }
}
