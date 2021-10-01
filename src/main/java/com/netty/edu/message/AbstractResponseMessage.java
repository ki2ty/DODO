package com.netty.edu.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName AbstractResponseMessage
 * @Author zlc
 * @Date 2021/10/1 下午11:53
 * @Description AbstractResponseMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractResponseMessage extends Message {

    private boolean success = true;
    private String reason;

}
