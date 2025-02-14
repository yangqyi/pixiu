package com.lab.sqlsession;

import java.util.List;

/**
 * @author 杨秋颐 
 * @since 2022-04-02 15:24
 */
public interface SqlSession {

    <E> List<E> selectList(String statementId,Object param) throws Exception;

    <T> T selectOne(String statementId,Object param) throws Exception;

    <T> T getMapper(Class<?> mapperClass);
}
