package com.chess.jnd.config;

import com.chess.jnd.converter.BytesToGameConverter;
import com.chess.jnd.converter.GameToBytesConverter;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.Arrays;

@Configuration
@EnableRedisRepositories(value = "com.chess.jnd.repository")
public class RedisConfiguration {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisProperties properties = redisProperties();
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        configuration.setHostName(properties.getHost());
        configuration.setPort(properties.getPort());

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<byte[], byte[]> redisTemplate() {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();

        template.setConnectionFactory(redisConnectionFactory());

        return template;
    }

    @Bean
    @Primary
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    @Bean
    public RedisCustomConversions redisCustomConversions(GameToBytesConverter gameToBytesConverter,
                                                         BytesToGameConverter bytesToGameConverter) {

        return new RedisCustomConversions(Arrays.asList(gameToBytesConverter, bytesToGameConverter));
    }
}