package ru.ivan.rsuproject.dto;

public record ScoreRequest(
    Integer age,
    Double income,
    Integer yearsAtJob,
    Integer dependents,
    Boolean hasExistingLoan,
    Integer creditScore
) {}
