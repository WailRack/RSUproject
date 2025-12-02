package ru.ivan.rsuproject.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.ivan.rsuproject.dto.ScoreRequest;
import ru.ivan.rsuproject.dto.ScoreResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringService {

    private final RuleBasedScoringService ruleBasedScoringService;
    private final LogisticRegressionScoringService logisticRegressionScoringService;

    public ScoreResponse score(ScoreRequest request) {
        double ruleScore = ruleBasedScoringService.calculateScore(request);
        double defaultProb = logisticRegressionScoringService.predictDefaultProbability(request);

        log.info("{}",ruleScore);
        log.info("{}", defaultProb);


        boolean approved = ruleScore >= 50 && defaultProb <= 0.5;
        log.info("{}", approved);

        return new ScoreResponse(ruleScore, defaultProb, approved);
    }
}
