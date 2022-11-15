package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Override
    public Transaction create(TransactionType type, Double amount, String description, LocalDateTime date, Account account) {
        Transaction transaction = new Transaction(type,amount,description,date,account);
        transactionRepository.save(transaction);

        if(type.equals(TransactionType.DEBITO))
            account.setBalance(account.getBalance()-amount);
        else
            account.setBalance(account.getBalance()+amount);
        accountRepository.save(account);
        return transaction;
    }
}
