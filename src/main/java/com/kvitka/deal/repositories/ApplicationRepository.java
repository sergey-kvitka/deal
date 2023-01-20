package com.kvitka.deal.repositories;

import com.kvitka.deal.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
