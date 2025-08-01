package ru.vinogradiya.utils.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@SuppressWarnings("NullableProblems")
public class IdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI(); // /products/123
        String[] parts = path.split("/");
        if (parts.length > 2) {
            try {
                UUID id = UUID.fromString(parts[parts.length - 1]);
                CurrentEntityIdHolder.set(id);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        CurrentEntityIdHolder.clear();
    }
}