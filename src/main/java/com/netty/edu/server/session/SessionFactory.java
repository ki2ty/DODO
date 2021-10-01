package com.netty.edu.server.session;

/**
 * @ClassName SessionFactory
 * @Author zlc
 * @Date 2021/10/2 上午1:36
 * @Description SessionFactory
 * @Version 1.0
 */
public abstract class SessionFactory {
    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }

}
