package com.netty.edu.conf;

import com.netty.edu.protocol.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName Config
 * @Author zlc
 * @Date 2021/10/9 下午6:56
 * @Description Config
 * @Version 1.0
 */
@Slf4j
public abstract class Config {
    static Properties properties;

    static {
        try (InputStream in = Config.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static int getServerPort() {
        final String value = properties.getProperty("server.port");
        if (value == null) {
            log.debug("no server.port in 'application.properties', use default '8080'");
            return 8080;
        }
        log.debug("server port '{}'", value);
        return Integer.parseInt(value);
    }

    public static Serializer.Algorithms getSerializerAlgorithms() {
        final String value = properties.getProperty("serializer.algorithms");
        if (value == null) {
            log.debug("no serializer.algorithms in 'application.properties', use default 'Serializer.Algorithms.Java'");
            return Serializer.Algorithms.Java;
        }
        log.debug("serializer algorithms(Java, Json) use '{}'", value);
        return Serializer.Algorithms.valueOf(value);
    }
}
