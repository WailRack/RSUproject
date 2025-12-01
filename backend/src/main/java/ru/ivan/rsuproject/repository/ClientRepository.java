package ru.ivan.rsuproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ivan.rsuproject.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
