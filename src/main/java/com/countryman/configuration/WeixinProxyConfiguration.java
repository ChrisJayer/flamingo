package com.countryman.configuration;

import com.countryman.properties.WeixinMpProperties;
import com.foxinmy.weixin4j.cache.RedisCacheStorager;
import com.foxinmy.weixin4j.model.Token;
import com.foxinmy.weixin4j.model.WeixinAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.mp.api.MassApi;
import com.foxinmy.weixin4j.mp.api.MediaApi;
import com.foxinmy.weixin4j.token.TokenManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * @author countryman
 * @mail chris_jay_9111@sina.com
 * @create 2018-06-19 11:37
 * @description
 **/
@Configuration
@EnableConfigurationProperties(WeixinMpProperties.class)
public class WeixinProxyConfiguration {


    @Bean
    public RedisCacheStorager<Token> tokenStorager(JedisPool jedisPool){
        return new RedisCacheStorager<>(jedisPool);
    }

    @Bean
    public WeixinAccount weixinAccount(WeixinMpProperties properties){
        return new WeixinAccount(properties.getAppid(), properties.getSecret());
    }

    @Bean
    public WeixinProxy weixinProxy(WeixinAccount account, RedisCacheStorager<Token> storager){
        return new WeixinProxy(account, storager);
    }

    @Bean
    public TokenManager tokenManager(WeixinProxy weixinProxy){
        return weixinProxy.getTokenManager();

    }

    @Bean
    public MediaApi mediaApi(TokenManager tokenManager){
        return new MediaApi(tokenManager);
    }


    @Bean
    public MassApi massApi(TokenManager tokenManager){
        return new MassApi(tokenManager);
    }


}
