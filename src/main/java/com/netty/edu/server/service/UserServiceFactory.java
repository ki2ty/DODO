package com.netty.edu.server.service;

/**
 * @ClassName UserServiceFactory
 * @Author zlc
 * @Date 2021/10/1 下午11:49
 * @Description UserServiceFactory
 * @Version 1.0
 */
public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getService() {
        return userService;
    }

}
