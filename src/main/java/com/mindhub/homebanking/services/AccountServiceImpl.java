package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account createAccount(Client client) {
        Account account = new Account("VIN-"+String.valueOf((int) (Math.random() * (10000000 - 1) + 1)),
                LocalDateTime.now(),0d,client);
        accountRepository.save(account);
        return account;
    }
}
