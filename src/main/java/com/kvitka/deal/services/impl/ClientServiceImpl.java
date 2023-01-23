package com.kvitka.deal.services.impl;

import com.kvitka.deal.entities.Client;
import com.kvitka.deal.repositories.ClientRepository;
import com.kvitka.deal.services.ClientService;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client entity) {
        return clientRepository.save(entity);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }
}
