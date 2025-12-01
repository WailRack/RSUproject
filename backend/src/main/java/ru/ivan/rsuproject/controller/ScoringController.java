package ru.ivan.rsuproject.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.ivan.rsuproject.dto.ScoreRequest;
import ru.ivan.rsuproject.dto.ScoreResponse;
import ru.ivan.rsuproject.service.ScoringService;

@RestController
@RequestMapping("/api/score")
@RequiredArgsConstructor
public class ScoringController {
    private final ScoringService scoringService;

    @PostMapping
    public ScoreResponse score(@RequestBody ScoreRequest request) {
        return scoringService.score(request);
    }
}
