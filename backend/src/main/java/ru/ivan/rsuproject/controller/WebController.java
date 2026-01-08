package ru.ivan.rsuproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.ivan.rsuproject.dto.HealthResponseDto;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WebController {

    @GetMapping("/ping")
    public Mono<ResponseEntity<String>> index() {
        return Mono.just(ResponseEntity.ok("Hello"));
    }

    @GetMapping("/health")
    public Mono<ResponseEntity<HealthResponseDto>> health() {
        return Mono.just(ResponseEntity.ok(new HealthResponseDto("OK")));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<HealthResponseDto>> notFoundException(IllegalArgumentException ex) {
        return Mono.just(ResponseEntity.badRequest().body(new HealthResponseDto(ex.getMessage())));
    }
}