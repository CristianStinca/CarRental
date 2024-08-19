package com.crististinca.CarRentalAPI.model;

import com.crististinca.CarRentalAPI.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    public Client findClientByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }
}
