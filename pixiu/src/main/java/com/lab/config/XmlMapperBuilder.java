package com.lab.config;

import com.lab.pojo.Configuration;
import com.lab.pojo.MapperStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * xmlMapper配置构建类
 *
 * @author 杨秋颐 
 * @since 2022-04-02 15:02
 */
public class XmlMapperBuilder {

    private Configuration configuration;

    public XmlMapperBuilder(Configuration configuration){
        this.configuration = configuration;
    }

    public void parse(InputStream resourceAsSteam) throws DocumentException {
        Document document = new SAXReader().read(resourceAsSteam);
        Element rootElement = document.getRootElement();

        List<Element> selectList = rootElement.selectNodes("//select");

        /*
        *    <select id="selectOne" resultType="com.lab.pojo.User" parameterType="com.lab.pojo.User">
               select * from user where id = #{id} and username = #{username}
             </select>
        * */
        String namespace = rootElement.attributeValue("namespace");
        for (Element element : selectList) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sql = element.getTextTrim();

            // 封装MappedStatement对象
            // statementId
            String statementId = namespace + "." + id;
            MapperStatement mapperStatement = new MapperStatement();
            mapperStatement.setStatementId(statementId);
            mapperStatement.setResultType(resultType);
            mapperStatement.setParameterType(parameterType);
            mapperStatement.setSqlText(sql);
            mapperStatement.setSqlCommandType("select");

            // 存储到configuration的map集合中
            configuration.getMapperStatementMap().put(statementId,mapperStatement);
        }
    }
}
