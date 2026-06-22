package com.ynova.medisalud.appointments.infrastructure.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.resolver.CommonHeaderResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CommonHeaderResolver commonHeaderResolver;

    public WebMvcConfig(CommonHeaderResolver commonHeaderResolver) {
        this.commonHeaderResolver = commonHeaderResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(commonHeaderResolver);
    }
}
