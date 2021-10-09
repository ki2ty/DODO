package com.netty.edu.protocol;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName Serializer
 * @Author zlc
 * @Date 2021/10/9 下午5:32
 * @Description Serializer
 * @Version 1.0
 */
public interface Serializer {

    //反序列化
    <T> T deserializer(Class<T> clazz, byte[] bytes);

    //序列化
    <T> byte[] serializer(T object);

    enum Algorithms implements Serializer {
        Java {
            @Override
            public <T> T deserializer(Class<T> clazz, byte[] bytes) {
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                try (ObjectInputStream ois = new ObjectInputStream(bis)) {
                    return (T) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("反序列化失败", e);
                }
            }

            @Override
            public <T> byte[] serializer(T object) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                    oos.writeObject(object);
                    return bos.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("序列化失败", e);
                }
            }
        },
        Json {
            @Override
            public <T> T deserializer(Class<T> clazz, byte[] bytes) {
                String str = new String(bytes, StandardCharsets.UTF_8);
                T object = JSON.parseObject(str, clazz);
                return object;
            }

            @Override
            public <T> byte[] serializer(T object) {
                return JSON.toJSONString(object).getBytes(StandardCharsets.UTF_8);
            }
        }
    }

}
