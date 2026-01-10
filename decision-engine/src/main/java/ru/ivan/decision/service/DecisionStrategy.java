package ru.ivan.decision.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ivan.decision.dto.DecisionResult;
import ru.ivan.decision.dto.ScoringComponents;
import ru.ivan.decision.dto.ScoringRequest;
import ru.ivan.decision.dto.ScoringResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class DecisionStrategy {

    private final RuleEngine ruleEngine;
    private final OnnxScoringService mlService;

    private static final double MIN_RULE_SCORE = 60.0;
    private static final double MAX_PD_ALLOWED = 0.40;
    private static final double MANUAL_REVIEW_PD_THRESHOLD = 0.30;

    public ScoringResponse assess(ScoringRequest request) {
        log.info("Starting assessment for request: {}", request);

        double ruleScore = ruleEngine.calculateRuleScore(request);

        if (ruleScore == 0.0) {
            log.info("Auto-reject by rules. Score: 0");
            return new ScoringResponse(
                DecisionResult.REJECTED,
                new ScoringComponents(0.0, -1.0, "FAIL_FAST_RULES"),
                "Critical rule violation (Age, Income or Eligibility)"
            );
        }

        double pd = mlService.predictProbability(request);
        log.info("ML Probability of Default: {}", pd);

        DecisionResult result;
        String reason;

        if (ruleScore < MIN_RULE_SCORE) {
            result = DecisionResult.REJECTED;
            reason = "Financial stability score too low (" + ruleScore + ")";
        } else if (pd > MAX_PD_ALLOWED) {
            result = DecisionResult.REJECTED;
            reason = "High credit risk (PD: " + String.format("%.2f", pd) + ")";
        } else if (pd > MANUAL_REVIEW_PD_THRESHOLD) {
            result = DecisionResult.MANUAL_REVIEW;
            reason = "Borderline risk profile. Manual review required.";
        } else {
            result = DecisionResult.APPROVED;
            reason = "Low risk. Approved.";
        }

        return new ScoringResponse(
            result,
            new ScoringComponents(ruleScore, pd, "STRATEGY_V1_HYBRID"),
            reason
        );
    }
}
