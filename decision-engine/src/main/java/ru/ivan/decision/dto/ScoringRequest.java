package ru.ivan.decision.dto;

public record ScoringRequest(
    Integer age,
    Double income,
    Integer yearsAtJob,
    Integer dependents,
    Boolean hasExistingLoan,
    Integer creditScore
) {}
