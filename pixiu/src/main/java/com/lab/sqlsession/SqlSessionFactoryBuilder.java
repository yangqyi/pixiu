package com.lab.sqlsession;

import com.lab.config.XmlConfigBuilder;
import com.lab.pojo.Configuration;

import java.io.InputStream;

/**
 * sqlSessionFactory构建类
 *
 * @author 杨秋颐 
 * @since 2022-04-01 16:37
 */
public class SqlSessionFactoryBuilder {


    public SqlSessionFactory build(InputStream inputStream) throws Exception {

        //解析配置文件，构建配置类
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(inputStream);
        Configuration configuration = xmlConfigBuilder.parse();

        //创建SqlSessionFactory实现类对象
        return new DefaultSqlSessionFactory(configuration);
    }
}
