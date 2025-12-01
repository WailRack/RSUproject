package ru.ivan.rsuproject.service;

import org.springframework.stereotype.Service;

import ru.ivan.rsuproject.dto.ScoreRequest;
import ru.ivan.rsuproject.dto.ScoreResponse;

@Service
public class ScoringService {

    private final RuleBasedScoringService ruleBasedScoringService;
    private final LogisticRegressionScoringService logisticRegressionScoringService;

    public ScoringService(RuleBasedScoringService ruleBasedScoringService,
                          LogisticRegressionScoringService logisticRegressionScoringService) {
        this.ruleBasedScoringService = ruleBasedScoringService;
        this.logisticRegressionScoringService = logisticRegressionScoringService;
    }

    public ScoreResponse score(ScoreRequest request) {
        double ruleScore = ruleBasedScoringService.calculateScore(request);
        double defaultProb = logisticRegressionScoringService.predictDefaultProbability(request);

        boolean approved = ruleScore >= 50 && defaultProb <= 0.4;

        return new ScoreResponse(ruleScore, defaultProb, approved);
    }
}
