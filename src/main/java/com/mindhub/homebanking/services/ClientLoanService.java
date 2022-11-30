package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

public interface ClientLoanService {
    List<ClientLoanDTO> getByClient(long client);
    List<ClientLoanDTO> getByAmounts(double amount);
    List<ClientLoanDTO> getByBalance(long client,double balance);

    Double add(Client client, Loan loan, Double amount, Integer payments);
}
