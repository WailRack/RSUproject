package ru.ivan.rsuproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ivan.rsuproject.dto.ClientRequestDto;
import ru.ivan.rsuproject.dto.ClientResponseDto;
import ru.ivan.rsuproject.dto.HealthResponseDto;
import ru.ivan.rsuproject.mapper.ClientMapper;
import ru.ivan.rsuproject.service.ClientService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WebController {

    private final ClientService clientService;

    @GetMapping("/ping")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponseDto> health() {
        return ResponseEntity.ok(new HealthResponseDto("OK"));
    }

    @PostMapping("/clients")
    public ResponseEntity<ClientResponseDto> createClient(@Validated @RequestBody ClientRequestDto dto) {
        var saved = clientService.create(dto);
        return ResponseEntity.ok(ClientMapper.toDto(saved));
    }

    @GetMapping("/clients")
    public ResponseEntity<List<ClientResponseDto>> getAll() {
        var list = clientService.findAll()
                .stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientResponseDto> getById(@PathVariable Long id) {
        var client = clientService.findById(id);
        return ResponseEntity.ok(ClientMapper.toDto(client));
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HealthResponseDto> notFoundException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new HealthResponseDto("s"));
    }
}
