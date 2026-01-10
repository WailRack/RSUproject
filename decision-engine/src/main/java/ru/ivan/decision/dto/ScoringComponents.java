package ru.ivan.decision.dto;

public record ScoringComponents(
    double ruleScore,            // 0.0 - 100.0
    double probabilityOfDefault, // 0.00 - 1.00
    String appliedStrategyId
) {}
