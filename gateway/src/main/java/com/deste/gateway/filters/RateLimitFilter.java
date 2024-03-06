package com.deste.gateway.filters;

import com.deste.gateway.domain.knowledgegraph.Key;
import com.deste.gateway.domain.knowledgegraph.KeyService;
import com.deste.gateway.domain.user.User;
import com.deste.gateway.domain.user.UserRepository;
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

import java.util.Collection;
import java.util.List;


@Component
@RequiredArgsConstructor
public class RateLimitFilter implements GatewayFilter {

    private final UserService userService;
    private final KeyService keyService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        List<String> authzHeaders = request.getHeaders().get("Authorization");

        String authzHeaderString = getAuthzHeaderString(authzHeaders);
        String[] split = authzHeaderString.split(" ");

        if (split[0].equals("Bearer") && split.length == 2) {
            String email = split[1];
            if (userService.isUserLimitReached(email)) {
                response.setStatusCode(HttpStatus.valueOf(402));
                return response.setComplete();
            }else {
                response.getHeaders().add("X-API-Group-Consumed", userService.getConsumedFor(email));

                //todo
            }
        }
        if (split[0].equals("Key") && split.length == 2) {
            String keyName = split[1];
            if (keyService.isKeyLimitReached(keyName)) {
                response.setStatusCode(HttpStatus.valueOf(402));
                return response.setComplete();
            }else {
                response.getHeaders().add("X-API-Group-Consumed", keyService.getConsumedFor(keyName));
                //todo
            }
        }

        return chain.filter(exchange);
    }

    private String getAuthzHeaderString(List<String> authzHeaders) {
        return String.valueOf(authzHeaders.get(0));
    }

}
