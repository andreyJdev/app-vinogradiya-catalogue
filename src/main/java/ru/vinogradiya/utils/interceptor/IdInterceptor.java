package ru.vinogradiya.utils.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import ru.vinogradiya.utils.enums.AdminPathPattern;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@SuppressWarnings("NullableProblems")
public class IdInterceptor implements HandlerInterceptor {

    private static final PathPatternParser PARSER = new PathPatternParser();
    private static final List<PathPattern> PATTERNS = Arrays.stream(AdminPathPattern.values())
            .map(pattern -> pattern.getApiPath().concat("/{id}"))
            .map(PARSER::parse)
            .toList();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        PathContainer pathContainer = PathContainer.parsePath(path);

        for (PathPattern pattern : PATTERNS) {
            PathPattern.PathMatchInfo matchInfo = pattern.matchAndExtract(pathContainer);
            if (matchInfo != null) {
                matchInfo.getUriVariables().values().stream()
                        .filter(IdInterceptor::isUUID)
                        .findFirst()
                        .map(UUID::fromString)
                        .ifPresent(CurrentEntityIdHolder::set);
                break;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        CurrentEntityIdHolder.clear();
    }

    private static boolean isUUID(String value) {
        try {
            UUID.fromString(value);
            log.info(">> Из адресной строки получен UUID: {}", value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}