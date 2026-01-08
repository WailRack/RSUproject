package ru.ivan.decision.dto;

public record ScoringResponse(
    DecisionResult decision,
    ScoringComponents components,
    String rejectReason
) {}
