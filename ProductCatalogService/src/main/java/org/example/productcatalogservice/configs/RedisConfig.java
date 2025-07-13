package org.example.productcatalogservice.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    // redis start commands in ubuntu
//    ravimeher@Bujji:~$ sudo service redis-server start
//[sudo] password for ravimeher:password
//    ravimeher@Bujji:~$ redis-cli
//127.0.0.1:6379> ping
//            PONG
}
