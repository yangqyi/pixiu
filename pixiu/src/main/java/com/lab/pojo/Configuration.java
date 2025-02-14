package com.lab.pojo;

import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置类
 *
 * @author 杨秋颐 
 * @since 2022-04-01 16:28
 */
@Data
public class Configuration {

    //数据源对象
    private DataSource dataSource;


    //用于存储解析好的mapperStatement  key:mapperStatementId,value:mapperStatement
    private Map<String,MapperStatement> mapperStatementMap = new HashMap<>();
}
