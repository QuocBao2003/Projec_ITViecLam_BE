package com.example.demo.config;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.lang.reflect.Array;
import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration= new CorsConfiguration();
//        cho phép các URL nào có thể kết nối tới backend
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:4173","http://localhost:5173"));
//        các method nào được kết nối
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        các phần header được gửi lên
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type","Accept","x-no-retry"));
//        gửi kèm cookies hay không
        configuration.setAllowCredentials(true);
//        thời gian pre-light request có thể cache(tiính theo second)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
}
