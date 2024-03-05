package com.deste.gateway.configuration;

import com.deste.gateway.filters.AuthFilterFake;
import com.deste.gateway.filters.CustomFilter;
import com.deste.gateway.filters.RateLimitFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("add_group_white", r -> r.path("/white")
                        .filters(f -> f.addResponseHeader("X-API-Group", "api_white")
                                .filter(new AuthFilterFake())
                                .filter(new RateLimitFilter())
                        )

                        .uri("http://localhost:8081"))
                .route("add_group_black", r -> r.path("/black")
                        .filters(f -> f.addResponseHeader("X-API-Group", "api_black")
                                .filter(new RateLimitFilter()))
                        .uri("http://localhost:8081"))
                .build();
    }
}
