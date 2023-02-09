package com.kvitka.deal.services.impl;

import com.kvitka.deal.entities.Credit;
import com.kvitka.deal.repositories.CreditRepository;
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
class CreditServiceImplTest {

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private CreditServiceImpl creditService;

    @Test
    @Order(1)
    @DisplayName("CreditService.save method test")
    void save() {
        creditService.save(new Credit());

        verify(creditRepository, times(1)).save(any());
    }

    @Test
    @Order(2)
    @DisplayName("CreditService.findById method test")
    void findById() {
        creditService.findById(Long.MAX_VALUE);

        verify(creditRepository, times(1)).findById(any());
    }
}