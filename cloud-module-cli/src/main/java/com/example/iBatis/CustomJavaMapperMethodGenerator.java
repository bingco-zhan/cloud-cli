package com.example.iBatis;

import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

public class CustomJavaMapperMethodGenerator extends AbstractJavaMapperMethodGenerator {

    @Override
    public void addInterfaceElements(Interface interfaze) {
        
        addInterfaceCheckOrElse(interfaze);
        addInterfaceSelectByColumn(interfaze);
        addInterfaceSelectByColumns(interfaze);
    }

    private void addInterfaceCheckOrElse(Interface interfaze) {
        // 先创建import对象
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        // 设置返回值类型
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("java.lang.Integer");
        importedTypes.add(returnType);
        // 创建方法对象
        Method method = new Method();
        // 设置该方法为public
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(returnType);
        method.setName("checkOrElse");

        // 设置参数类型是对象
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("java.io.Serializable");
        importedTypes.add(parameterType);

        // 给方法添加参数
        Parameter id = new Parameter(parameterType, "id");
        // 给参数添加注解
        id.addAnnotation("@Param(\"id\")");
        method.addParameter(id);

        parameterType = new FullyQualifiedJavaType("java.lang.Byte");
        importedTypes.add(parameterType);

        Parameter status = new Parameter(parameterType, "status");
        status.addAnnotation("@Param(\"status\")");
        method.addParameter(status);

        parameterType = new FullyQualifiedJavaType("java.lang.Long");
        importedTypes.add(parameterType);

        Parameter cfmuser = new Parameter(parameterType, "cfmuser");
        cfmuser.addAnnotation("@Param(\"cfmuser\")");
        method.addParameter(cfmuser);

        addMapperAnnotations(interfaze, method);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    private void addInterfaceSelectSelective(Interface interfaze) {
        // 先创建import对象
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        // 添加Lsit的包
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        // 创建方法对象
        Method method = new Method();
        // 设置该方法为public
        method.setVisibility(JavaVisibility.PUBLIC);
        // 设置返回类型是List
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        returnType.addTypeArgument(listType);
        
        // 方法对象设置返回类型对象
        method.setReturnType(returnType);
        // 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
        method.setName("selectSelective");

        // 设置参数类型是对象
        FullyQualifiedJavaType parameterType;
        parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        // import参数类型对象
        importedTypes.add(parameterType);
        // 为方法添加参数，变量名称record
        method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$
        //
        addMapperAnnotations(interfaze, method);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }
    
    private void addInterfaceSelectByColumn(Interface interfaze) {
        // 先创建import对象
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        // 添加Lsit的包
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        // 创建方法对象
        Method method = new Method();
        // 设置该方法为public
        method.setVisibility(JavaVisibility.PUBLIC);
        // 设置返回类型是List
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType;
        // 设置List的类型是实体类的对象
        listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        importedTypes.add(listType);
        // 返回类型对象设置为List
        returnType.addTypeArgument(listType);
        // 方法对象设置返回类型对象
        method.setReturnType(returnType);
        // 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
        method.setName("selectByCloumn");

        // 设置参数类型是对象
        FullyQualifiedJavaType parameterType;
        parameterType = new FullyQualifiedJavaType("java.lang.String");
        // import参数类型对象
        importedTypes.add(parameterType);
        // 为方法添加参数，变量名称record
        Parameter cloumn = new Parameter(parameterType, "cloumn");
        cloumn.addAnnotation("@Param(\"cloumn\")");
        method.addParameter(cloumn);
        
        parameterType = new FullyQualifiedJavaType("java.io.Serializable");
        // import参数类型对象
        importedTypes.add(parameterType);
        // 为方法添加参数，变量名称record
        Parameter value = new Parameter(parameterType, "value");
        value.addAnnotation("@Param(\"value\")");
        
        importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        method.addParameter(value);
        
        //
        addMapperAnnotations(interfaze, method);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }
    
    private void addInterfaceSelectByColumns(Interface interfaze) {
        // 先创建import对象
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        // 添加Lsit的包
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        // 创建方法对象
        Method method = new Method();
        // 设置该方法为public
        method.setVisibility(JavaVisibility.PUBLIC);
        // 设置返回类型是List
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType;
        // 设置List的类型是实体类的对象
        listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        importedTypes.add(listType);
        // 返回类型对象设置为List
        returnType.addTypeArgument(listType);
        // 方法对象设置返回类型对象
        method.setReturnType(returnType);
        // 设置方法名称为我们在IntrospectedTable类中初始化的 “selectByObject”
        method.setName("selectByCloumns");

        // 设置参数类型是对象
        FullyQualifiedJavaType parameterType;
        parameterType = new FullyQualifiedJavaType("java.lang.String");
        // import参数类型对象
        importedTypes.add(parameterType);
        // 为方法添加参数，变量名称record
        Parameter cloumn = new Parameter(parameterType, "cloumn1");
        cloumn.addAnnotation("@Param(\"cloumn1\")");
        method.addParameter(cloumn);
        
        parameterType = new FullyQualifiedJavaType("java.io.Serializable");
        // import参数类型对象
        importedTypes.add(parameterType);
        // 为方法添加参数，变量名称record
        Parameter value = new Parameter(parameterType, "value1");
        value.addAnnotation("@Param(\"value1\")");
        method.addParameter(value);
        
        parameterType = new FullyQualifiedJavaType("java.lang.String");
        cloumn = new Parameter(parameterType, "cloumn2");
        cloumn.addAnnotation("@Param(\"cloumn2\")");
        method.addParameter(cloumn);
        
        parameterType = new FullyQualifiedJavaType("java.io.Serializable");
        // import参数类型对象
        importedTypes.add(parameterType);
        // 为方法添加参数，变量名称record
        value = new Parameter(parameterType, "value2");
        value.addAnnotation("@Param(\"value2\")");
        
        importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        method.addParameter(value);
        
        //
        addMapperAnnotations(interfaze, method);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }
}
