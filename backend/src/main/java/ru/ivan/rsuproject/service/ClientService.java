package ru.ivan.rsuproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ivan.rsuproject.dto.ClientRequestDto;
import ru.ivan.rsuproject.entity.Client;
import ru.ivan.rsuproject.mapper.ClientMapper;
import ru.ivan.rsuproject.repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client create(ClientRequestDto dto) {
        Client toSave = ClientMapper.toEntity(dto);
        return clientRepository.save(toSave);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));
    }
}