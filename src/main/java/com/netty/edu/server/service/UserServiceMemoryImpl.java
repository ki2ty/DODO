package com.netty.edu.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName UserServiceMemoryImpl
 * @Author zlc
 * @Date 2021/10/1 下午10:38
 * @Description UserServiceMemoryImpl
 * @Version 1.0
 */
public class UserServiceMemoryImpl implements UserService {

    private Map<String, String> userMap = new ConcurrentHashMap<>();

    {
        userMap.put("zlc", "123qwe");
        userMap.put("admin", "admin");
        userMap.put("zzz", "1234");
    }


    @Override
    public boolean login(String userName, String password) {
        String pw = userMap.get(userName);
        if (pw == null) {
            return false;
        }
        return pw.equals(password);
    }
}
