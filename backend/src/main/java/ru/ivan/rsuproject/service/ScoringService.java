package ru.ivan.rsuproject.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import ru.ivan.rsuproject.dto.ScoreRequest;
import ru.ivan.rsuproject.dto.ScoreResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringService {

    private final ScoringProvider scoringProvider;

    public Mono<ScoreResponse> score(ScoreRequest request) {
        return scoringProvider.calculateScore(request);
    }
}
