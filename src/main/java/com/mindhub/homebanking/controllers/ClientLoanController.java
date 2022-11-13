package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientloans")
public class ClientLoanController {
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    AccountRepository accountRepository;

    /*
        ClientLoanRepository:
    -Buscar una lista de clientLoan por cliente
    -Buscar una lista de ClientLoan que sean mayores a x monto pasado por parametro
    -Buscar una lista de ClientLoan por cliente que  en cual sus balances sean menores a x monto pasado por parametro
     */

    @RequestMapping("/byclient/{client}")
    List<ClientLoanDTO> getByClient(@PathVariable long client){
        return clientLoanRepository.findByClientId(client).stream().map(ClientLoanDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/byamounts/{amount}")
    List<ClientLoanDTO> getByAmounts(@PathVariable double amount){
        return clientLoanRepository.findByAmountGreaterThan(amount).stream().map(ClientLoanDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/bybalance/{client}/{balance}")
    List<ClientLoanDTO> getByBalance(@PathVariable long client, @PathVariable double balance){
        return clientLoanRepository.findByClientAndAmountGreaterThan(client, balance).stream().map(ClientLoanDTO::new)
                .collect(Collectors.toList());
    }

    @RequestMapping("/clientswithbalacelower/{balance}")
    List<ClientLoanDTO> getByBalance(@PathVariable double balance){
        Set<ClientLoan> clientLoans = new HashSet<>();
        Set<Account> accounts = accountRepository.findAll().stream().filter(account -> {return account.getBalance() < balance; }).
                collect(Collectors.toSet());
        Set<Client> clients = new HashSet<>();
        for(Account account:accounts){
            clients.add(account.getClient());
            System.out.println("account "+account.getId());
        }

        for(Client client :clients) {
            clientLoans.addAll(client.getClientLoans());
            System.out.println("client "+client.getId());
        }
        return clientLoans.stream().map(ClientLoanDTO::new).collect(Collectors.toList());

    }

}
