package com.lab.pojo;

import lombok.Data;

/**
 * sql语句解析结果
 *
 * @author 杨秋颐 
 * @since 2022-04-01 16:19
 */
@Data
public class MapperStatement {

    //唯一标识 namespace+id
    private String statementId;

    //返回类型
    private String resultType;

    //入参类型
    private String parameterType;

    //sql语句
    private String sqlText;

    //sql类型
    private String sqlCommandType;
}
