package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByDateBetween(LocalDateTime desde, LocalDateTime hasta);
    List<Transaction> findByAmountGreaterThanAndAndAmountLessThan(double amount, double amount2);
    List<Transaction> findByType(TransactionType type);
}
