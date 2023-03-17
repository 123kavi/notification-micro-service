package com.notification.configuration;

import com.notification.util.Utility;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CachingConfig {

    /**
     * @return org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
     * Defines System specific Redis Cache Configurations
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> {
            Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
            configurationMap.put(Utility.REDIS_CACHE_MAP_KEY_SEND_EMAIL, RedisCacheConfiguration.defaultCacheConfig());
            builder.withInitialCacheConfigurations(configurationMap);
        };
    }
}