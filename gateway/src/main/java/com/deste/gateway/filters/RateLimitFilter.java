package com.deste.gateway.filters;

import com.deste.gateway.domain.user.User;
import com.deste.gateway.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
@RequiredArgsConstructor
public class RateLimitFilter implements GatewayFilter {

    private final UserRepository userRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        ServerHttpResponse response = exchange.getResponse();
//        List<String> authzHeaders = request.getHeaders().get("Authorization");
//
//        if(authzHeaders.size() ==1 && getAuthzHeaderString(authzHeaders).contains("Bearer")) {
//
////            List<User> userCalling = userRepository.findByEmail();
//        }

        return chain.filter(exchange);
    }

    private static String getAuthzHeaderString(List<String> authzHeaders) {
        return String.valueOf(authzHeaders.get(0).toCharArray());
    }
}
