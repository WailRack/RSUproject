package ru.ivan.rsuproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientRequestDto {

    @NotBlank
    private String fullName;

    @NotNull
    @Min(0)
    private Integer age;

    @NotNull
    @Min(0)
    private Double income;
}