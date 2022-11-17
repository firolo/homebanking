package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
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
        transactionRepository.save(trans2);
        accountRepository.save(account1);
        accountRepository.save(account2);
        return true;
    }
    @Override
    public List<TransactionDTO> getByDateBetween(String fechadesde, String fechahasta) {
        return transactionRepository.findByDateBetween(LocalDateTime.parse(fechadesde), LocalDateTime.parse(fechahasta)).
                stream().map(TransactionDTO::new).collect(Collectors.toList());
    }
    @Override
    public List<TransactionDTO> getByAmountBetween(Double amount1, Double amount2) {
        return transactionRepository.findByAmountGreaterThanAndAndAmountLessThan(amount1,amount2).stream().
                map(TransactionDTO::new).collect(Collectors.toList());
    }
    @Override
    public List<TransactionDTO> getByType(TransactionType type) {
        return transactionRepository.findByType(type).stream().map(TransactionDTO::new).collect(Collectors.toList());
    }
}
