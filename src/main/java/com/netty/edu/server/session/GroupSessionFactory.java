package com.netty.edu.server.session;

/**
 * @ClassName GroupSessionFactory
 * @Author zlc
 * @Date 2021/10/2 上午1:37
 * @Description GroupSessionFactory
 * @Version 1.0
 */
public abstract class GroupSessionFactory {

    private static GroupSession groupSession = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return groupSession;
    }

}
