package com.deste.gateway.filters;

import com.deste.gateway.domain.knowledgegraph.Key;
import com.deste.gateway.domain.knowledgegraph.KeyRepository;
import com.deste.gateway.domain.knowledgegraph.KnowledgeGraph;
import com.deste.gateway.domain.knowledgegraph.KnowledgeGraphRepository;
import com.deste.gateway.domain.user.User;
import com.deste.gateway.domain.user.UserRepository;
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

    private final UserRepository userRepo;
    private final KeyRepository keyRepo;
    private final KnowledgeGraphRepository knowledgeGraphRepo;

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
            List<User> users = userRepo.findByEmail(split[1]);
            if (users != null && users.size() == 1)
                return true;
        }
        if (split[0].equals("Key")) {
            List<Key> keys = keyRepo.findByName(split[1]);
            if (keys != null && keys.size() == 1) {
                Key key = keys.get(0);
                return key.getKnowledgeGraph() != null && key.getKnowledgeGraph().getUser() != null;
            }
        }
        return false;
    }

}
