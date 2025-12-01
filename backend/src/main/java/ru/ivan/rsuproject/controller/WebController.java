package ru.ivan.rsuproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivan.rsuproject.dto.HealthResponseDto;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WebController {

    @GetMapping("/ping")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponseDto> health() {
        return ResponseEntity.ok(new HealthResponseDto("OK"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HealthResponseDto> notFoundException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new HealthResponseDto(ex.getMessage()));
    }
}
