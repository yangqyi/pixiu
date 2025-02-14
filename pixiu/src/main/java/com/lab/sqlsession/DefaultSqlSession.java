package com.lab.sqlsession;

import com.lab.executor.Executor;
import com.lab.pojo.Configuration;
import com.lab.pojo.MapperStatement;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

/**
 * @author 杨秋颐 
 * @since 2022-04-02 15:28
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;


    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object param) throws Exception {
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        if(null == mapperStatement) throw new RuntimeException("无法获取到相应的sql");

        List<E> list = executor.query(configuration,mapperStatement,param);
        return list;
    }

    @Override
    public <T> T selectOne(String statementId, Object param) throws Exception {
        List<Object> list = this.selectList(statementId, param);
        if(list.isEmpty()){
            return null;
        }else if(list.size() == 1){
            return (T)list.get(0);
        }else{
            throw new RuntimeException("查询结果过多...");
        }
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        Object mapper = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{mapperClass}, (o,method,args) -> {

            //获取statementId
            Class<?> declaringClass = method.getDeclaringClass();
            String className = declaringClass.getName();
            String methodName = method.getName();
            String statementId = className + "." + methodName;

            //获取相应的mapperStatement
            MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
            if(null == mapperStatement) throw new RuntimeException("无法获取到相应的sql");

            //根据sql类型（select,update,delete）执行相应的方法
            String sqlCommandType = mapperStatement.getSqlCommandType();
            switch (sqlCommandType){
                case("select"):
                    //获取方法的返回类型
                    Class<?> returnTypeClass = method.getReturnType();

                    //判断返回类型是不是集合类
                    boolean assignableFrom = Collections.class.isAssignableFrom(returnTypeClass);
                    if(assignableFrom){
                        String parameterType = mapperStatement.getParameterType();
                        if(null != parameterType){
                            return selectList(statementId,args[0]);
                        }
                        return selectList(statementId,null);
                    }
                    //返回类型不是集合执行selectOne
                    return selectOne(statementId,args[0]);
                case "update":
                    // 更新操作
                    break;
                case "insert":
                    // 更新操作
                    break;
                case "delete":
                    // 更新操作
                    break;

            }

            return null;
        });
        return (T) mapper;
    }
}
