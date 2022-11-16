package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public interface TransactionService {
    public boolean create(double amount, String description, LocalDateTime date, Account account1, Account account2);
}
