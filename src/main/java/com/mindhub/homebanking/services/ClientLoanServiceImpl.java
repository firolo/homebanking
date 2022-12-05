package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientLoanServiceImpl implements ClientLoanService {
    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Override
    public List<ClientLoanDTO> getByClient(long client){
        return clientLoanRepository.findByClientId(client).stream().map(ClientLoanDTO::new).collect(Collectors.toList());
    }
    @Override
    public List<ClientLoanDTO> getByAmounts(double amount){
        return clientLoanRepository.findByAmountGreaterThan(amount).stream().map(ClientLoanDTO::new).collect(Collectors.toList());
    }
    @Override
    public List<ClientLoanDTO> getByBalance(long client, double balance){
        return clientLoanRepository.findByClientAndAmountGreaterThan(client, balance).stream().map(ClientLoanDTO::new)
                .collect(Collectors.toList());
    }
    @Override
    public Double add(Client client, Loan loan, Double amount, Integer payments) {
        Double finalAmount = amount;// + amount * loan.getPercent() /100;
        ClientLoan clientLoan = new ClientLoan(client,loan,finalAmount, payments);
        clientLoanRepository.save(clientLoan);
        return finalAmount;
    }
}
