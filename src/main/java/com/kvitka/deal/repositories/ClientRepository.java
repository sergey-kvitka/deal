package com.kvitka.deal.repositories;

import com.kvitka.deal.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
