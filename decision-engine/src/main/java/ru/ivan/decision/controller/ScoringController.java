package ru.ivan.decision.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ivan.decision.dto.ScoringRequest;
import ru.ivan.decision.dto.ScoringResponse;
import ru.ivan.decision.service.DecisionStrategy;

@RestController
@RequestMapping("/api/v1/decisions")
@RequiredArgsConstructor
public class ScoringController {

    private final DecisionStrategy decisionStrategy;

    @PostMapping
    public ScoringResponse makeDecision(@RequestBody ScoringRequest request) {
        return decisionStrategy.assess(request);
    }
}