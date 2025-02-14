package com.lab.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.lab.io.Resource;
import com.lab.pojo.Configuration;
import lombok.Data;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * xml配置装配类
 *
 * @author 杨秋颐 
 * @since 2022-04-01 16:44
 */
@Data
public class XmlConfigBuilder {

    private InputStream xmlInpuStream;

    public XmlConfigBuilder(InputStream xmlInpuStream){
        this.xmlInpuStream = xmlInpuStream;
    }

    public Configuration parse() throws Exception {
        if(null == xmlInpuStream) throw new Exception("配置文件流为空...");

        //读取配置文件
        Document document = new SAXReader().read(this.xmlInpuStream);
        Element rootElement = document.getRootElement();

        //获取<property>标签
        List<Element> nodes = rootElement.selectNodes("//property");
        if(nodes.isEmpty()) throw new Exception("配置文件错误：无<property>标签");

        //获取<property>标签name,value属性，并封装到properties中
        Properties properties = new Properties();
        for (Element element : nodes) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.put(name,value);
        }

        //创建数据库连接库
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(properties.getProperty("driverClassName"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));

        //创建全局配置类
        Configuration configuration = new Configuration();
        configuration.setDataSource(dataSource);

        List<Element> mapperList = rootElement.selectNodes("//mapper");
        for (Element element : mapperList) {
            String mapperPath = element.attributeValue("resource");
            InputStream resourceAsSteam = Resource.getResourceAsStream (mapperPath);
            XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
            xmlMapperBuilder.parse(resourceAsSteam);
        }


        return configuration;
    }

}
