package ru.ivan.decision.dto;

public record ScoringResponse(
    boolean approved,
    double probability,
    String decisionReason
) {}
