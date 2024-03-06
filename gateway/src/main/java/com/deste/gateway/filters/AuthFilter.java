package com.deste.gateway.filters;

import com.deste.gateway.domain.knowledgegraph.KeyService;
import com.deste.gateway.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter implements GatewayFilter {

    private final UserService userService;
    private final KeyService keyService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        List<String> authzHeaders = request.getHeaders().get("Authorization");
        if (authzHeaders == null || authzHeaders.size() != 1) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        } else {
            char[] authValue = authzHeaders.get(0).toCharArray();
            if (!isValidToken(authValue)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }
        return chain.filter(exchange);
    }

    private boolean isValidToken(char[] authValue) {
        String authValueString = String.valueOf(authValue);
        String[] split = authValueString.split(" ");
        if (split.length != 2) return false;
        if (split[0].equals("Bearer")) {
            return userService.isValidUser(split[1]);
        }
        if (split[0].equals("Key")) {
            return keyService.isValidKey(split[1]);
        }
        return false;
    }

}
