package com.countryman.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author countryman
 * @mail chris_jay_9111@sina.com
 * @create 2018-06-19 11:38
 * @description
 **/
@Data
@ConfigurationProperties(prefix = "wechat.mp")
public class WeixinMpProperties {

    private String appid;

    private String secret;

    private String token;
}
