package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountApplicationDTO;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionService transactionService;

    @Override
    public Account createAccount(Client client) {
        Account account = new Account("VIN-"+String.valueOf((int) (Math.random() * (10000000 - 1) + 1)),
                LocalDateTime.now(),0d,client, AccountType.CAJA_AHORRO);
        accountRepository.save(account);
        return account;
    }

    @Override
    public Set<AccountDTO> getAll() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    @Override
    public AccountDTO getAccount(long id) {
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @Override
    public List<AccountDTO> getAccountByBalanceGreaterThan(double balance) {
        return accountRepository.findByBalanceGreaterThan(balance).stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> findByCreationDateBefore(String date) {
        return accountRepository.findByCreationDateBefore(LocalDateTime.parse(date)).stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> findByNumber(String number) {
        return accountRepository.findByNumber(number).stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public Account accountByNumber(String number) {
        List<Account> acc = accountRepository.findByNumber(number);
        return acc.isEmpty()? null : acc.iterator().next();
    }

    @Override
    public Set<Account> accountsBalanceLessThan(double balance) {
        return accountRepository.findAll().stream().filter(account -> {return account.getBalance() < balance; }).
                collect(Collectors.toSet());
    }

    @Override
    public void deleteAccount(AccountApplicationDTO accountDTO) {
        Account account = accountRepository.findById(accountDTO.getId()).orElse(null);
        /* si tiene transacciones las elimino */
        Set<Transaction> transactions = account.getTransactions();
        if(!transactions.isEmpty()) {
            transactionService.deleteSet(transactions);
        }

        if(account != null) {
            account.setActive(false);
            accountRepository.save(account);
        }
    }
}
