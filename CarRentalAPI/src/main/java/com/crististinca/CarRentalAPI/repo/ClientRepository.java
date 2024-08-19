package com.crististinca.CarRentalAPI.repo;

import com.crististinca.CarRentalAPI.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
}
