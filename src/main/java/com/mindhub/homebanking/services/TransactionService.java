package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public interface TransactionService {
    public boolean create(double amount, String description, LocalDateTime date, Account account1, Account account2);
    public boolean createForLoan(double amount, String description, Account account1);
    List<TransactionDTO> getByDateBetween( String fechadesde,  String fechahasta);
    List<TransactionDTO> getByAmountBetween( Double amount1,  Double amount2);
    List<TransactionDTO> getByType( TransactionType type);
}
