package com.example;

import com.example.constants.BannerAndAttributeConfig;
import com.example.controller.ControllerConfig;
import com.example.database.DatabaseConfig;
import com.example.database.JavaBeanConfig;
import com.example.iBatis.IBatisConfig;
import com.example.service.ServiceConfig;

/**
 * 主入口函数
 *
 * @Author: bingco
 * @Version: v1.0
 * @Description:
 * @Created: 2018年12月31日 11:54
 * @Package: adm-workspace - com.example.main
 * <p>
 * -------------------------------------------------------------------
 */
public class Main {

    public static void main(String[] args) {
        DatabaseConfig.builder();
        // 加载基本信息录入
        BannerAndAttributeConfig.builder();
        // 加载IBatis模版构建
        IBatisConfig.builder();
        // 加载VO类构建
        JavaBeanConfig.builder();
        // 加载service层的模版构建
        ServiceConfig.builder();
        // 加载controller层的模版构建
        ControllerConfig.builder();
    }
}
