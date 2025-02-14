package com.lab.executor;

import com.lab.pojo.Configuration;
import com.lab.pojo.MapperStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * 执行器对象
 * @author 杨秋颐 
 * @since 2022-04-02 15:25
 */
public interface Executor {

    <E> List<E> query(Configuration configuration, MapperStatement mapperStatement, Object param) throws Exception;
}
