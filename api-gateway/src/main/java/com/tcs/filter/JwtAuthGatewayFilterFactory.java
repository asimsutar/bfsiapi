package com.tcs.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.tcs.util.JwtUtil;

import reactor.core.publisher.Mono;
@Component
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config>{
	@Autowired
    private JwtUtil jwtUtil;

    public JwtAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

           // Allow login & register without JWT
            String path = exchange.getRequest().getURI().getPath();

            if (path.contains("/auth/login") || path.contains("/users/register")) {
                return chain.filter(exchange);
            }

          // Check Authorization header
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return unauthorized(exchange);
            }

            String authHeader =
                    exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorized(exchange);
            }

            String token = authHeader.substring(7);

          // Validate JWT
            if (!jwtUtil.validateToken(token)) {
                return unauthorized(exchange);
            }

          // Forward request
            return chain.filter(exchange);
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config {}
}
