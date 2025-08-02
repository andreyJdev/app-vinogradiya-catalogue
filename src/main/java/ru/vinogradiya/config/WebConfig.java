package ru.vinogradiya.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.vinogradiya.utils.enums.AdminPathPattern;
import ru.vinogradiya.utils.interceptor.IdInterceptor;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final IdInterceptor idInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(idInterceptor)
                .addPathPatterns(Arrays.stream(AdminPathPattern.values())
                        .map(pattern -> pattern.getMvcPath().concat("/*"))
                        .toArray(String[]::new));
    }
}