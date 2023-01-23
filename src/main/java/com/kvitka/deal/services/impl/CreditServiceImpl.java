package com.kvitka.deal.services.impl;

import com.kvitka.deal.entities.Credit;
import com.kvitka.deal.repositories.CreditRepository;
import com.kvitka.deal.services.CreditService;
import org.springframework.stereotype.Service;

@Service
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    public CreditServiceImpl(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Override
    public Credit save(Credit entity) {
        return creditRepository.save(entity);
    }

    @Override
    public Credit findById(Long id) {
        return creditRepository.findById(id).orElse(null);
    }
}
