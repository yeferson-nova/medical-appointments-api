package com.ynova.medisalud.appointments.infrastructure.adapter.in.web.filter;

import java.io.IOException;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyFilter.class);
    private static final String HEADER_NAME = "X-Idempotency-Key";
    private static final String REDIS_PREFIX = "idempotency:";
    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate redis;

    public IdempotencyFilter(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        return !"POST".equalsIgnoreCase(method) && !"PATCH".equalsIgnoreCase(method);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String idempotencyKey = request.getHeader(HEADER_NAME);
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            chain.doFilter(request, response);
            return;
        }

        String redisKey = REDIS_PREFIX + idempotencyKey;
        String cached = redis.opsForValue().get(redisKey);

        if (cached != null) {
            log.info("Idempotency replay for key={}", idempotencyKey);
            response.setContentType("application/json");
            response.setStatus(extractStatus(cached));
            response.getWriter().write(extractBody(cached));
            return;
        }

        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        chain.doFilter(request, wrappedResponse);

        int status = wrappedResponse.getStatus();
        String body = new String(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding());

        if (status >= 200 && status < 300) {
            String value = status + "|" + body;
            redis.opsForValue().set(redisKey, value, TTL);
            log.debug("Idempotency key stored: key={}", idempotencyKey);
        }

        wrappedResponse.copyBodyToResponse();
    }

    private int extractStatus(String cached) {
        int separatorIndex = cached.indexOf('|');
        if (separatorIndex > 0) {
            return Integer.parseInt(cached.substring(0, separatorIndex));
        }
        return 200;
    }

    private String extractBody(String cached) {
        int separatorIndex = cached.indexOf('|');
        if (separatorIndex > 0) {
            return cached.substring(separatorIndex + 1);
        }
        return cached;
    }
}
