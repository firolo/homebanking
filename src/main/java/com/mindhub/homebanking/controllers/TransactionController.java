package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountService accountService;
    @Autowired
    ClientService clientService;

    @GetMapping("/transactions/bydate/{fechadesde}/{fechahasta}")
    List<TransactionDTO> getByDateBetween(@PathVariable String fechadesde, @PathVariable String fechahasta) {
        return transactionService.getByDateBetween(fechadesde,fechahasta);
    }

    @GetMapping("/transactions/amountbetween/{amount1}/{amount2}")
    List<TransactionDTO> getByAmountBetween(@PathVariable Double amount1, @PathVariable Double amount2) {
        return transactionService.getByAmountBetween(amount1,amount2);
    }

    @GetMapping("/transactions/bytype/{type}")
    List<TransactionDTO> getByType(@PathVariable TransactionType type) {
        return transactionService.getByType(type);
    }

    @PostMapping("/transactions")
    ResponseEntity<Object> createTransaction(Authentication authentication, @RequestParam String fromAccountNumber,
                                             @RequestParam String toAccountNumber, @RequestParam Double amount,
                                             @RequestParam String description
                                             ) {


        if(amount.toString().isEmpty() || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Incomplete parameters", HttpStatus.FORBIDDEN);
        }

        if(fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Accounts must be different", HttpStatus.FORBIDDEN);
        }

        Account account1 = accountService.accountByNumber(fromAccountNumber);
        if(account1==null) {
            return new ResponseEntity<>("Origin account does not exist", HttpStatus.FORBIDDEN);
        }

        if(clientService.isAccountFromClient(authentication.getName().toString(), fromAccountNumber)) {
            return new ResponseEntity<>("The origin account does not belong to client", HttpStatus.FORBIDDEN);
        }

        Account account2 = accountService.accountByNumber(toAccountNumber);
        if(account2==null) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }

        if(account1.getBalance() < amount) {
            return new ResponseEntity<>("Origen account has not got enough money", HttpStatus.FORBIDDEN);
        }

        transactionService.create(amount, description, LocalDateTime.now(), account1, account2);


        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
