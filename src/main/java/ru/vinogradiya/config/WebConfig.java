package ru.vinogradiya.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.vinogradiya.utils.interceptor.IdInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final IdInterceptor idInterceptor;

    public WebConfig(IdInterceptor idInterceptor) {
        this.idInterceptor = idInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(idInterceptor);
    }
}