package ru.ivan.rsuproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.ivan.rsuproject.dto.ScoreRequest;
import ru.ivan.rsuproject.dto.ScoreResponse;

@Slf4j
@Service
public class RemoteScoringProvider implements ScoringProvider {

    private final WebClient webClient;

    public RemoteScoringProvider(WebClient.Builder webClientBuilder, @Value("${decision-engine.url}") String decisionEngineUrl) {
        this.webClient = webClientBuilder.baseUrl(decisionEngineUrl).build();
    }

    @Override
    public Mono<ScoreResponse> calculateScore(ScoreRequest request) {
        log.info("Using Remote Scoring Provider via {}", webClient);
        
        record RemoteResponse(boolean approved, double probability, String decisionReason) {}

        return webClient.post()
            .uri("/api/v1/decisions")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(RemoteResponse.class)
            .map(response -> {
                // Mapping:
                // ruleScore -> 100.0 if approved, 0.0 if not (Dummy value)
                double dummyRuleScore = response.approved() ? 100.0 : 0.0;
                return new ScoreResponse(dummyRuleScore, response.probability(), response.approved());
            });
    }
}