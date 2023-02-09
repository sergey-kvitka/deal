package com.kvitka.deal.services.impl;

import com.kvitka.deal.entities.Application;
import com.kvitka.deal.repositories.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Test
    @Order(1)
    @DisplayName("ApplicationService.save method test")
    void save() {
        applicationService.save(new Application());

        verify(applicationRepository, times(1)).save(any());
    }

    @Test
    @Order(2)
    @DisplayName("ApplicationService.findById method test")
    void findById() {
        applicationService.findById(Long.MAX_VALUE);

        verify(applicationRepository, times(1)).findById(any());
    }

    @Test
    @Order(3)
    @DisplayName("ApplicationService.findAll method test")
    void findAll() {
        List<Application> list = applicationService.findAll();

        assertNotEquals(null, list);

        verify(applicationRepository, times(1)).findAll();
    }
}