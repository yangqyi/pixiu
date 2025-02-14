package com.lab.sqlsession;

/**
 * sqlSession工厂类
 * @author 杨秋颐 
 * @since 2022-04-01 16:39
 */
public interface SqlSessionFactory {

    public SqlSession openSession();

}
