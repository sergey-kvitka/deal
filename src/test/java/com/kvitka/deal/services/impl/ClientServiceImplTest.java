package com.kvitka.deal.services.impl;

import com.kvitka.deal.entities.Client;
import com.kvitka.deal.repositories.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    @Order(1)
    @DisplayName("ClientService.save method test")
    void save() {
        clientService.save(new Client());

        verify(clientRepository, times(1)).save(any());
    }

    @Test
    @Order(2)
    @DisplayName("ClientService.findById method test")
    void findById() {
        clientService.findById(Long.MAX_VALUE);

        verify(clientRepository, times(1)).findById(any());
    }
}