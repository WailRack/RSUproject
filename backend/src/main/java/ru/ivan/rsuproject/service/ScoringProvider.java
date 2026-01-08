package ru.ivan.rsuproject.service;

import reactor.core.publisher.Mono;
import ru.ivan.rsuproject.dto.ScoreRequest;
import ru.ivan.rsuproject.dto.ScoreResponse;

public interface ScoringProvider {
    Mono<ScoreResponse> calculateScore(ScoreRequest request);
}