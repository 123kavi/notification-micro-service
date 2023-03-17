//package com.cloudofgoods.notification.configuration.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class InterceptorConfiguration implements WebMvcConfigurer {
//    @Value("${app.redis.client-secret}")
//    public String clientSecret;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new BasicAuthenticationInterceptor(clientSecret));
//    }
//}
