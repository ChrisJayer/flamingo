package com.countryman.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author countryman
 * @mail chris_jay_9111@sina.com
 * @create 2018-06-01 15:53
 * @description
 **/
@Data
@ConfigurationProperties(prefix = "infra.redis")
public class InfraRedisProperties {

    private String host;
    private int port;
    private String password;
    private int defaultdb;
    private int timeout;
    private int maxTotal;
    private int maxIdle;
    private int maxWait;
}
