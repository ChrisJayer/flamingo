package com.countryman.configuration;

import com.countryman.properties.InfraRedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author countryman
 * @mail chris_jay_9111@sina.com
 * @create 2018-06-19 11:44
 * @description
 **/
@Configuration
@EnableConfigurationProperties(InfraRedisProperties.class)
public class RedisConfiguration {

    @Autowired
    private InfraRedisProperties redisProperties;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
        jedisPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getMaxWait());
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {

//        JedisClientConfiguration configuration = JedisClientConfiguration.defaultConfiguration();

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setHostName(redisProperties.getHost());
        jedisConnectionFactory.setPort(redisProperties.getPort());
        jedisConnectionFactory.setPassword(redisProperties.getPassword());
        jedisConnectionFactory.setTimeout(redisProperties.getTimeout());
        jedisConnectionFactory.setDatabase(redisProperties.getDefaultdb());
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }


    @Bean
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {
        return new JedisPool(jedisPoolConfig,
                redisProperties.getHost(), redisProperties.getPort(),
                redisProperties.getTimeout(), redisProperties.getPassword(),
                redisProperties.getDefaultdb());
    }
}
