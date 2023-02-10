package com.kvitka.deal.repositories;

import com.kvitka.deal.entities.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Long> {
}
