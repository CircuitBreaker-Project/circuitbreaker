package com.circuitbreaker.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRateLimitGatewayFilterFactory
        extends AbstractGatewayFilterFactory<InMemoryRateLimitGatewayFilterFactory.Config> {

    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public InMemoryRateLimitGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            String clientKey = resolveClientKey(exchange);

            TokenBucket bucket = buckets.computeIfAbsent(
                    clientKey,
                    key -> new TokenBucket(
                            config.getCapacity(),
                            config.getRefillPerSecond()
                    )
            );

            boolean allowed = bucket.tryConsume();

            exchange.getResponse().getHeaders().set(
                    "X-RateLimit-Limit",
                    String.valueOf(config.getCapacity())
            );

            exchange.getResponse().getHeaders().set(
                    "X-RateLimit-Remaining",
                    String.valueOf(bucket.getRemaining())
            );

            if (!allowed) {

                exchange.getResponse().setStatusCode(
                        HttpStatus.TOO_MANY_REQUESTS
                );

                exchange.getResponse().getHeaders().set(
                        "Retry-After",
                        "1"
                );

                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    private String resolveClientKey(ServerWebExchange exchange) {

        String forwardedFor = exchange.getRequest()
                .getHeaders()
                .getFirst("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        if (exchange.getRequest().getRemoteAddress() != null
                && exchange.getRequest().getRemoteAddress().getAddress() != null) {

            return exchange.getRequest()
                    .getRemoteAddress()
                    .getAddress()
                    .getHostAddress();
        }

        return "anonymous";
    }

    public static class Config {

        private int capacity = 10;
        private int refillPerSecond = 5;

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public int getRefillPerSecond() {
            return refillPerSecond;
        }

        public void setRefillPerSecond(int refillPerSecond) {
            this.refillPerSecond = refillPerSecond;
        }
    }

    private static class TokenBucket {

        private final int capacity;
        private final int refillPerSecond;

        private double tokens;
        private long lastRefillTime;

        TokenBucket(int capacity, int refillPerSecond) {
            this.capacity = capacity;
            this.refillPerSecond = refillPerSecond;
            this.tokens = capacity;
            this.lastRefillTime = System.nanoTime();
        }

        synchronized boolean tryConsume() {

            refill();

            if (tokens >= 1) {
                tokens--;
                return true;
            }

            return false;
        }

        synchronized int getRemaining() {

            refill();

            return (int) Math.floor(tokens);
        }

        private void refill() {

            long now = System.nanoTime();

            double elapsedSeconds =
                    (now - lastRefillTime) / 1_000_000_000.0;

            tokens = Math.min(
                    capacity,
                    tokens + elapsedSeconds * refillPerSecond
            );

            lastRefillTime = now;
        }
    }
}