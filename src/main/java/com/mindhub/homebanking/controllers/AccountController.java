package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public Set<AccountDTO> getAll() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id){
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }
    /*
    AccountRepository:
    -Buscar una lista de cuentas en el cual su balance se mayor a x monto pasado por parametro
    -Buscar una lista de cuentas por en la cual sue fecha se menor a la que le pasemos por parametro
    -Buscar una cuenta por Numero de cuenta
    */

    @RequestMapping("/accounts/balance/{balance}")
    List<AccountDTO> getAccountByBalanceGreaterThan(@PathVariable double balance) {
        return accountRepository.findByBalanceGreaterThan(balance).stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/accounts/creationdate/{localdatetime}")
    List<AccountDTO> findByCreationDateBefore(@PathVariable String localdatetime) {
        return accountRepository.findByCreationDateBefore(LocalDateTime.parse(localdatetime)).stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/accounts/number/{number}")
    List<AccountDTO> findByNumber(@PathVariable String number) {
        return accountRepository.findByNumber(number).stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if(client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("It is allowed a maximum of three accounts per client", HttpStatus.FORBIDDEN);
        }
        accountRepository.save(new Account("VIN-"+String.valueOf((int) (Math.random() * (99999999 - 1) + 1)),LocalDateTime.now(),0d,client));
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
