package com.example.demo;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@SpringBootConfiguration
public class AppConfig {

    @Profile("caching")
    @EnableCaching
    @Configuration
    public static class CachingConfig {

        public static final String POST_CACHE = "postCache";

        @Bean
        public RedisCacheConfiguration cacheConfiguration() {
            return RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(60))
                    .disableCachingNullValues()
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        }

        @Bean
        public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
            return (builder) -> builder
                    .withCacheConfiguration(POST_CACHE,
                            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(10)));
        }

    }
}
