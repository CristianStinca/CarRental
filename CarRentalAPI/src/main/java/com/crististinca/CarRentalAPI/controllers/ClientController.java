package com.crististinca.CarRentalAPI.controllers;

import com.crististinca.CarRentalAPI.model.CarService;
import com.crististinca.CarRentalAPI.model.Client;
import com.crististinca.CarRentalAPI.model.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/by/email")
    public ResponseEntity<Client> getByEmail(@RequestParam String email) {
        Optional<Client> maybeClient = Optional.ofNullable(clientService.findClientByEmail(email));

        if (maybeClient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(maybeClient.get());
    }

    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.addClient(client));
    }
}
