//package com.example.gateway_service.filter;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.net.http.HttpHeaders;
//import java.util.Objects;
//import java.util.logging.Logger;
//
//@Component
//public class AuthAndApiKeyValidationGlobalFilter implements GlobalFilter, Ordered {
//    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
//    private static final String AUTHORIZATION_HEADER = "Authorization";
//    private static final String API_KEY_HEADER = "X-API-KEY";
//    private static final Logger log = LoggerFactory.getLogger(AuthAndApiKeyValidationGlobalFilter.class);
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Value("${application.security.api-key}")
//    private String apiKey;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
//        String correlationId = getCorrelationId(exchange);
//        String authorization = getToken(exchange);
//        String apiKey = getApiKey(exchange);
//
//        if (StringUtils.isNotBlank(apiKey) && !validateApiKey(apiKey)) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        if (StringUtils.isNotBlank(authorization) && !validateToken(authorization)) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        ServerWebExchange modifiedExchange = exchange.mutate()
//                .request(r -> r.headers(headers -> {
//                    headers.set(HttpHeaders.AUTHORIZATION, authorization);
//                    headers.set(CORRELATION_ID_HEADER, correlationId);
//                }))
//                .build();
//
//        return chain.filter(modifiedExchange);
//    }
//
//    private String getCorrelationId(ServerWebExchange exchange) {
//        HttpHeaders headers = exchange.getRequest().getHeaders();
//        return headers.getFirst(CORRELATION_ID_HEADER) != null ? headers.getFirst(CORRELATION_ID_HEADER) : UUID.randomUUID().toString();
//    }
//
//    private String getToken(ServerWebExchange exchange) {
//        HttpHeaders headers = exchange.getRequest().getHeaders();
//        return headers.getFirst(AUTHORIZATION_HEADER) != null ? Objects.requireNonNull(headers.getFirst(AUTHORIZATION_HEADER)) : "";
//    }
//
//    private String getApiKey(ServerWebExchange exchange) {
//        HttpHeaders headers = exchange.getRequest().getHeaders();
//        return headers.getFirst(API_KEY_HEADER) != null ? Objects.requireNonNull(headers.getFirst(API_KEY_HEADER)) : "";
//    }
//
//    private boolean validateApiKey(String apiKeyHeader) {
//        return apiKeyHeader != null && apiKeyHeader.equals(apiKey);
//    }
//
//    private boolean validateToken(String token) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(token.replace("Bearer ", ""))
//                    .getBody();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}