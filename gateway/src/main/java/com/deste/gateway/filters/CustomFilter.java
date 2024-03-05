package com.deste.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter() {
        super(Config.class);
    }

    public static class Config {
        // Put configuration properties here
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Custom pre-processing logic here
            System.out.println("Pre-processing logic here");

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // Custom post-processing logic here
                System.out.println("Post-processing logic here");
            }));
        };
    }
}
