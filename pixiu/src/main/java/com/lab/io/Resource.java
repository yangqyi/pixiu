package com.lab.io;

import java.io.InputStream;

/**
 * 资源加载类
 *
 * @author 杨秋颐 
 * @since 2022-04-01 16:14
 */
public class Resource {

    /**
     * 根据路径获取输入流
     * @param path
     * @return
     */
    public static InputStream getResourceAsStream(String path) throws Exception {
        InputStream resourceAsStream = Resource.class.getClassLoader().getResourceAsStream(path);
        if(null == resourceAsStream) throw new Exception("无法加载配置文件："+path);
        return resourceAsStream;
    }
}
