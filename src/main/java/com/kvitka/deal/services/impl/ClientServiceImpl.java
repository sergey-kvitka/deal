package com.kvitka.deal.services.impl;

import com.kvitka.deal.entities.Client;
import com.kvitka.deal.repositories.ClientRepository;
import com.kvitka.deal.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client save(Client entity) {
        log.info("Sending client to the repository to save it to the database (client = {})", entity);
        Client client = clientRepository.save(entity);
        log.info("Client sent to the repository and successfully saved to the database as: {}", client);
        return client;
    }

    @Override
    public Client findById(Long id) {
        log.info("Sending client ID to the repository to receive it from the database (client ID = {})", id);
        Client client = clientRepository.findById(id).orElse(null);
        if (null == client) {
            log.info("Unable to find client with ID={} in database. <null> will be returned", id);
            return null;
        }
        log.info("client ID sent to the repository and Client received and will be returned ({})",
                client);
        return client;
    }
}
