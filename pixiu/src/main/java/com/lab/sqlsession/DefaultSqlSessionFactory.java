package com.lab.sqlsession;

import com.lab.executor.Executor;
import com.lab.executor.SimpleExecutor;
import com.lab.pojo.Configuration;

/**
 * 默认的sqlSessionFactory
 *
 * @author 杨秋颐 
 * @since 2022-04-02 15:20
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {

        //创建执行器对象
        Executor executor = new SimpleExecutor();



        return new DefaultSqlSession(configuration,executor);
    }
}
