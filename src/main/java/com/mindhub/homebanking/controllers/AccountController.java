package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public Set<AccountDTO> getAll() {
        return accountService.getAll();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id){
        return accountService.getAccount(id);
    }

    @RequestMapping("/accounts/balance/{balance}")
    List<AccountDTO> getAccountByBalanceGreaterThan(@PathVariable double balance) {
        return accountService.getAccountByBalanceGreaterThan(balance);
    }

    @RequestMapping("/accounts/creationdate/{localdatetime}")
    List<AccountDTO> findByCreationDateBefore(@PathVariable String localdatetime) {
        return accountService.findByCreationDateBefore(localdatetime);
    }

    @RequestMapping("/accounts/number/{number}")
    List<AccountDTO> findByNumber(@PathVariable String number) {
        return accountService.findByNumber(number);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if(client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("It is allowed a maximum of three accounts per client", HttpStatus.FORBIDDEN);
        }
        accountService.createAccount(client);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
