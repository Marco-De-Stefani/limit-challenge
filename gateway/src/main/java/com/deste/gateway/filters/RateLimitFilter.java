package com.deste.gateway.filters;

import com.deste.gateway.domain.knowledgegraph.KeyService;
import com.deste.gateway.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
@RequiredArgsConstructor
public class RateLimitFilter implements GatewayFilter {

    private final UserService userService;
    private final KeyService keyService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var authzHeaders = request.getHeaders().get("Authorization");
        var authzHeaderString = getAuthzHeaderString(authzHeaders);
        var split = authzHeaderString.split(" ");

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            var response = exchange.getResponse();
            var apiGroups = response.getHeaders().get("X-API-Group");
            if (apiGroups != null && apiGroups.size() == 1) {
                var apiGroup = apiGroups.get(0);
                if (isUser(split)) {
                    var email = split[1];
                    if (userService.isUserLimitReached(email, apiGroup)) {
                        response.setStatusCode(HttpStatus.valueOf(402));
                        return;
                    } else {
                        response.getHeaders().add("X-API-Group-Consumed", userService.getConsumedFor(email, apiGroup));
                        response.getHeaders().add("X-API-Group-User-Limit", userService.getTotalFor(email, apiGroup));
                    }
                }
                if (isKey(split)) {
                    String keyName = split[1];
                    if (keyService.isKeyLimitReached(keyName, apiGroup)) {
                        response.setStatusCode(HttpStatus.valueOf(402));
                    } else {
                        response.getHeaders().add("X-API-Group-Consumed", keyService.getConsumedFor(keyName, apiGroup));
                        response.getHeaders().add("X-API-Group-Key-Limit", keyService.getTotalFor(keyName, apiGroup));
                    }
                }
            }
        }));
    }

    private static boolean isKey(String[] split) {
        return split[0].equals("Key") && split.length == 2;
    }

    private static boolean isUser(String[] split) {
        return split[0].equals("Bearer") && split.length == 2;
    }

    private String getAuthzHeaderString(List<String> authzHeaders) {
        return String.valueOf(authzHeaders.get(0));
    }

}
