package ru.ivan.rsuproject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter(correlationIdFilter());
    }

    private ExchangeFilterFunction correlationIdFilter() {
        return (request, next) -> Mono.deferContextual(ctx -> {
            String correlationId = ctx.getOrDefault(CorrelationFilter.CORRELATION_ID_KEY, null);
            ClientRequest clientRequest = request;
            if (correlationId != null) {
                clientRequest = ClientRequest.from(request)
                        .header(CorrelationFilter.CORRELATION_ID_HEADER, correlationId)
                        .build();
            }
            return next.exchange(clientRequest);
        });
    }
}