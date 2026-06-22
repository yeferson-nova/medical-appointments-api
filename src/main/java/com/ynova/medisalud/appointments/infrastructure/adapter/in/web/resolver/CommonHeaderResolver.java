package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.resolver;

import java.time.Instant;
import java.util.UUID;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ynova.medisalud.appointments.infrastructure.adapter.in.web.dto.CommonHeader;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonHeaderResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(CommonHeader.class);
    }

    @Override
    public CommonHeader resolveArgument(MethodParameter parameter,
                                        ModelAndViewContainer mavContainer,
                                        NativeWebRequest webRequest,
                                        WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            return new CommonHeader(generateRequestId(), "unknown", null, null, Instant.now());
        }

        String requestId = request.getHeader("X-Request-ID");
        if (requestId == null || requestId.isBlank()) {
            requestId = generateRequestId();
        }

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isBlank()) {
            clientIp = request.getRemoteAddr();
        }

        return new CommonHeader(
                requestId,
                clientIp,
                request.getHeader("User-Agent"),
                request.getHeader("X-Idempotency-Key"),
                Instant.now()
        );
    }

    private String generateRequestId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
