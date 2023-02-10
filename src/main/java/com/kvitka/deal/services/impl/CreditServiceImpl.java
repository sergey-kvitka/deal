package com.kvitka.deal.services.impl;

import com.kvitka.deal.entities.Credit;
import com.kvitka.deal.repositories.CreditRepository;
import com.kvitka.deal.services.CreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    @Override
    public Credit save(Credit entity) {
        log.info("Sending credit to the repository to save it to the database (credit = {})", entity);
        Credit credit = creditRepository.save(entity);
        log.info("Credit sent to the repository and successfully saved to the database as: {}", credit);
        return credit;
    }

    @Override
    public Credit findById(Long id) {
        log.info("Sending credit ID to the repository to receive it from the database (credit ID = {})", id);
        Credit credit = creditRepository.findById(id).orElse(null);
        if (null == credit) {
            log.info("Unable to find credit with ID={} in database. <null> will be returned", id);
            return null;
        }
        log.info("credit ID sent to the repository and Credit received and will be returned ({})",
                credit);
        return credit;
    }
}
