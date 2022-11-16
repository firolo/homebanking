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
    public boolean create(double amount, String description, LocalDateTime date, Account account1, Account account2) {
        Transaction trans1 = new Transaction(TransactionType.DEBITO,amount,description+' '+account2.getNumber(),date,account1);
        Transaction trans2 = new Transaction(TransactionType.CREDITO,amount,description+' '+account2.getNumber(),date,account2);
        transactionRepository.save(trans1);

        accountRepository = null;
        transactionRepository.save(trans2);

        account1.setBalance(account1.getBalance()-amount);
        account2.setBalance(account2.getBalance()+amount);
        accountRepository.save(account1);
        accountRepository.save(account2);


        return true;
    }
}
