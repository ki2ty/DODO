package com.netty.edu.server.service;

/**
 * @ClassName UserService
 * @Author zlc
 * @Date 2021/10/1 下午10:36
 * @Description UserService 用户管理接口
 * @Version 1.0
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return 登陆成功返回true，否则返回false
     */
    boolean login(String userName, String password);

}
