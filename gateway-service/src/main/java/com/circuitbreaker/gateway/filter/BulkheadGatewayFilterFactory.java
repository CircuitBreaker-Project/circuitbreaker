package com.circuitbreaker.gateway.filter;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BulkheadGatewayFilterFactory
        extends AbstractGatewayFilterFactory<BulkheadGatewayFilterFactory.Config> {

    private final BulkheadRegistry bulkheadRegistry;

    public BulkheadGatewayFilterFactory(BulkheadRegistry bulkheadRegistry) {
        super(Config.class);
        this.bulkheadRegistry = bulkheadRegistry;
    }

    @Override
    public GatewayFilter apply(Config config) {

        Bulkhead bulkhead = bulkheadRegistry.bulkhead(config.getName());

        return (exchange, chain) -> {

            if (!bulkhead.tryAcquirePermission()) {

                if (!exchange.getResponse().isCommitted()) {

                    exchange.getResponse().getHeaders().set(
                            "X-Bulkhead-Rejected",
                            "true"
                    );

                    exchange.getResponse().setStatusCode(
                            HttpStatus.SERVICE_UNAVAILABLE
                    );
                }

                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange)
                    .doFinally(signal -> bulkhead.onComplete());
        };
    }

    public static class Config {

        private String name = "recommendationBulkhead";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}   