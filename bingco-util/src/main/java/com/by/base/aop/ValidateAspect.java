package com.by.base.aop;

import com.by.base.exce.BaseMessageException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
public class ValidateAspect {

    private static Logger logger = Logger.getLogger(ValidateAspect.class);

    @Autowired private SqlSessionFactory sqlSessionFactory;

    private final static String REGEX_OPERATION = "\\s+(([eq])*|([ne])*|([ge])*|([le])*|([gt])*|([lt])*)\\s+";

    private final static Map<String, String> OPE = new HashMap<>();

    static {
        OPE.put("eq", "=");
        OPE.put("ne", "!=");
        OPE.put("ge", ">=");
        OPE.put("le", "<=");
        OPE.put("lt", "<");
        OPE.put("gt", ">");
    }

    @Pointcut("@annotation(com.by.base.aop.NotNullValidate)")
    public void notNullValidatePointCut() {
    }

    @Pointcut("@annotation(com.by.base.aop.Validate)")
    public void validatePointCut() {
    }

    @Around("notNullValidatePointCut() || validatePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        NotNullValidate notNullValidate = method.getAnnotation(NotNullValidate.class);
        if (notNullValidate != null) {
            boolean result = validate(notNullValidate.table(), new String[]{"id eq " + notNullValidate.id()}, signature.getParameterNames(), joinPoint.getArgs());
            if (!result) {
                throw new RuntimeException("数据不存在!");
            }
        }

        Validate validate = method.getAnnotation(Validate.class);
        if (validate != null) {
            String[] wheres = validate.wheres();
            if (wheres.length > 0) {
                boolean result = validate(validate.table(), wheres, signature.getParameterNames(), joinPoint.getArgs());
                if (result == validate.resultBoolean()) {
                    throw new BaseMessageException(validate.message());
                }
            }
        }
        return joinPoint.proceed();
    }

    private boolean validate(String table, String[] wheres, String[] parameterNames, Object[] args) {
        boolean flag = false;
        StringBuilder sql = new StringBuilder("SELECT IF(COUNT(1) > 0, 1, 0) as flag FROM ");
        try {

            sql.append(table).append(" WHERE 1 = 1 ");

            for (String where : wheres) {

                String operation = getOperation(REGEX_OPERATION, where);
                if (operation == null) {
                    throw new RuntimeException("未检测到运算符!");
                }

                String[] split = where.split(REGEX_OPERATION);

                if (split.length == 2) {

                    String[] field = split[1].split("\\s?[.]\\s?");

                    sql.append(" AND ").append(split[0]).append(" ").append(OPE.get(operation.trim()));

                    if (split[1].matches("#\\(.+\\)")) {
                        sql.append(split[1].replace("#(","").replace(")", "").trim());
                        continue;
                    }

                    Object value = getParameter(parameterNames, args, field);

                    if (value == null || "".equals(value.toString().trim())) {
                        throw new RuntimeException(String.format("[%s]无法获取到参数!", field));
                    }

                    sql.append(" '").append(value.toString().trim()).append("' ");
                    logger.info("getParameter -------------->>>: " + value);

                } else {
                    throw new RuntimeException(String.format("[%s]表达式不正确!", where));
                }
            }

            logger.info("sql------------->>>: " + sql.toString());
            SqlSession session = SqlSessionUtils.getSqlSession(sqlSessionFactory);
            Connection connection = session.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql.toString());
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            flag = resultSet.getBoolean("flag");
            SqlSessionUtils.closeSqlSession(session, sqlSessionFactory);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    private Object getParameter(String[] parameterNames, Object[] args, String field[]) throws NoSuchFieldException {
        Object arg = null;
        for (int x = 0; x < parameterNames.length; x++) {
            if (parameterNames[x].equals(field[0])) {
                arg = args[x];
            }
        }

        if (field.length > 1 && arg != null) {
            if (arg instanceof Serializable) {
                return null;
            }
            try {
                Field privateField = arg.getClass().getDeclaredField(field[1]);
                privateField.setAccessible(true);
                return privateField.get(arg);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        } else return arg;
    }

    private String getOperation(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        boolean hasStr = matcher.find();
        if (hasStr) {
            return matcher.group();
        }
        else return null;
    }
}
