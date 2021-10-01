package com.netty.edu.server.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;

/**
 * @ClassName Group
 * @Author zlc
 * @Date 2021/10/1 下午11:06
 * @Description Group
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    private String groupName;

    private Set<String> members;

    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());

}
