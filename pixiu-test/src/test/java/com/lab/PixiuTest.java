package com.lab;

import java.io.InputStream;
import java.util.List;

import com.lab.io.Resource;
import com.lab.pojo.User;
import com.lab.sqlsession.SqlSession;
import com.lab.sqlsession.SqlSessionFactory;
import com.lab.sqlsession.SqlSessionFactoryBuilder;
import org.junit.Test;

/**
 * 貔貅测试类
 *
 * @author 杨秋颐 
 * @since 2022-04-02 16:39
 */
public class PixiuTest {
    /**
     * 传统方式测试（不使用mapper代理方式）
     */
    @Test
    public void test1() throws Exception {
        InputStream resourceAsStream = Resource.getResourceAsStream("sqlMapConfig.xml");

        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);

        SqlSession sqlSession = build.openSession();
        User user = new User();
        user.setId(1);
        user.setUsername("tom");

        User user1= sqlSession.selectOne("user.selectOne", user);

    }


    /**
     * 传统方式测试（不使用mapper代理方式）
     */
    @Test
    public void test2() throws Exception {



    }
}
