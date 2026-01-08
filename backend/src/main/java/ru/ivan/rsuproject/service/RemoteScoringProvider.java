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
        
        // Define internal DTOs matching the Decision Engine's new contract
        record ComponentsDto(double ruleScore, double probabilityOfDefault, String appliedStrategyId) {}
        record DecisionEngineResponse(String decision, ComponentsDto components, String rejectReason) {}

        return webClient.post()
            .uri("/api/v1/decisions")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(DecisionEngineResponse.class)
            .map(resp -> {
                log.info("Received decision: {}", resp);
                boolean approved = "APPROVED".equals(resp.decision());
                
                double pd = -1.0;
                double rScore = 0.0;
                
                if (resp.components() != null) {
                    pd = resp.components().probabilityOfDefault();
                    rScore = resp.components().ruleScore();
                } else if (resp.decision().equals("REJECTED") && resp.rejectReason().contains("FAIL_FAST")) {
                    // Handle fail-fast where components might be partial or null (though our engine returns them)
                     // In our impl, components are always present even on fail fast, but good to be safe.
                }

                return new ScoreResponse(rScore, pd, approved);
            });
    }
}
