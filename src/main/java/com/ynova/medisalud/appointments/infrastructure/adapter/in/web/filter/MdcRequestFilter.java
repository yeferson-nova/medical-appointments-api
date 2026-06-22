package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcRequestFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";
    private static final String CLIENT_IP = "clientIp";
    private static final String HTTP_METHOD = "httpMethod";
    private static final String REQUEST_URI = "requestUri";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            String requestId = request.getHeader("X-Request-ID");
            if (requestId == null || requestId.isBlank()) {
                requestId = UUID.randomUUID().toString().substring(0, 8);
            }

            MDC.put(REQUEST_ID, requestId);
            MDC.put(CLIENT_IP, resolveClientIp(request));
            MDC.put(HTTP_METHOD, request.getMethod());
            MDC.put(REQUEST_URI, request.getRequestURI());

            response.setHeader("X-Request-ID", requestId);

            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
