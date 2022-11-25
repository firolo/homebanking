package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientloans")
public class ClientLoanController {
    @Autowired
    ClientLoanService clientLoanService;
    @Autowired
    AccountService accountService;
    @GetMapping("/byclient/{client}")
    List<ClientLoanDTO> getByClient(@PathVariable long client){
        return clientLoanService.getByClient(client);
    }

    @GetMapping("/byamounts/{amount}")
    List<ClientLoanDTO> getByAmounts(@PathVariable double amount){
        return clientLoanService.getByAmounts(amount);
    }

    @GetMapping("/bybalance/{client}/{balance}")
    List<ClientLoanDTO> getByBalance(@PathVariable long client, @PathVariable double balance){
        return clientLoanService.getByBalance(client,balance);
    }

    @GetMapping("/clientswithbalacelower/{balance}")
    List<ClientLoanDTO> getByBalance(@PathVariable double balance){
        Set<ClientLoan> clientLoans = new HashSet<>();
        Set<Account> accounts = accountService.accountsBalanceLessThan(balance);
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
