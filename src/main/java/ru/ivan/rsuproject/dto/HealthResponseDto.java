package ru.ivan.rsuproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HealthResponseDto {

    private String message;
}
