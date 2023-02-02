package com.kvitka.deal.services;

import com.kvitka.deal.entities.Application;

import java.util.List;

public interface ApplicationService extends DatabaseEntityService<Application> {
    List<Application> findAll();
}
