package ru.ivan.rsuproject.mapper;

import ru.ivan.rsuproject.dto.ClientRequestDto;
import ru.ivan.rsuproject.dto.ClientResponseDto;
import ru.ivan.rsuproject.entity.Client;

public final class ClientMapper {

    private ClientMapper() {}

    public static Client toEntity(ClientRequestDto dto) {
        var client = new Client();
        client.setFullName(dto.getFullName());
        client.setAge(dto.getAge());
        client.setIncome(dto.getIncome());
        return client;
    }

    public static ClientResponseDto toDto(Client client) {
        return new ClientResponseDto(
                client.getId(),
                client.getFullName(),
                client.getAge(),
                client.getIncome()
        );
    }
}
