package com.wak.game.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class ApplicationConfig {

    /**
     * Ingress Nginx Controller에서 전달된 X-Forwarded-For, X-Forwarded-Proto, X-Forwarded-Port 헤더를 사용하기 위한 필터
     *
     * @return
     */
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}
