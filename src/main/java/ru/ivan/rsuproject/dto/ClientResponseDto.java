package ru.ivan.rsuproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientResponseDto {
    private Long id;
    private String fullName;
    private Integer age;
    private Double income;
}