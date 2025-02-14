package com.lab.executor;

import com.lab.config.BoundSql;
import com.lab.pojo.Configuration;
import com.lab.pojo.MapperStatement;
import com.lab.utils.GenericTokenParser;
import com.lab.utils.ParameterMapping;
import com.lab.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单执行器类
 *
 * @author 杨秋颐 
 * @since 2022-04-02 15:26
 */
public class SimpleExecutor implements Executor{


    @Override
    public <E> List<E> query(Configuration configuration, MapperStatement mapperStatement, Object param) throws Exception {

        //获取相应的sql语句
        String sqlText = mapperStatement.getSqlText();
        BoundSql boundSql = getBoundSql(sqlText);

        //获取数据库连接
        Connection connection = configuration.getDataSource().getConnection();

        //获取预编译对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getFinalSql());

        //封装入参到预编译对象
        if(null != param){
            String parameterType = mapperStatement.getParameterType();
            if(null != parameterType){
                Class<?> aClass = Class.forName(parameterType);

                //设置参数
                List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
                if(!parameterMappings.isEmpty()){
                    for (int i = 0;i < parameterMappings.size();i++){
                        ParameterMapping parameterMapping = parameterMappings.get(i);
                        //获取到字段名称
                        String content = parameterMapping.getContent();

                        //通过暴力反射获取到param中对应字段的值
                        Field declaredField = aClass.getDeclaredField(content);
                        declaredField.setAccessible(true);
                        Object o = declaredField.get(param);

                        preparedStatement.setObject(i+1,o);
                    }
                }
            }
        }
        //执行sql
        ResultSet resultSet = preparedStatement.executeQuery();

        //封装结果集
        String resultType = mapperStatement.getResultType();
        Class<?> resultClass = Class.forName(resultType);

        List<E> list = new ArrayList<>();
        while(resultSet.next()){
            ResultSetMetaData metaData = resultSet.getMetaData();

            Object o = resultClass.newInstance();
            for (int i = 0;i < metaData.getColumnCount();i++){
                String name = metaData.getColumnClassName(i + 1);
                Object object = resultSet.getObject(name);

                //内省设置字段值
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(name,resultClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,object);
            }

            list.add((E) o);

        }

        return null;
    }

    private BoundSql getBoundSql(String sqlText) {
        // 标记处理器：配合标记解析器完成标记的解析工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        // 带有？的sql语句
        String finalSql = genericTokenParser.parse(sqlText);

        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return new BoundSql(finalSql,parameterMappings);

    }
}
