package ru.ivan.rsuproject.dto;


public record ScoreResponse(
    double ruleScore,
    double mlProbability,
    boolean approved
) {}
