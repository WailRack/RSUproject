package ru.ivan.decision.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ivan.decision.dto.ScoringRequest;

@Slf4j
@Service
public class RuleEngine {

    public double calculateRuleScore(ScoringRequest request) {
        log.debug("Calculating rule score for request: {}", request);
        double score = 100.0;

        if (request.age() == null || request.age() < 18 || request.age() > 70) {
            log.info("Rule failed: Age invalid ({})", request.age());
            return 0.0;
        }

        if (request.income() == null || request.income() < 10000) {
            log.info("Rule failed: Income too low ({})", request.income());
            return 0.0;
        }

        if (request.yearsAtJob() != null) {
            if (request.yearsAtJob() < 1) {
                score -= 20.0;
            } else if (request.yearsAtJob() < 3) {
                score -= 10.0;
            }
        } else {
            score -= 20.0;
        }

        if (request.dependents() != null && request.dependents() > 0) {
            score -= request.dependents() * 5.0;
        }

        if (Boolean.TRUE.equals(request.hasExistingLoan())) {
            score -= 15.0;
        }

        return Math.max(0.0, score);
    }
}
