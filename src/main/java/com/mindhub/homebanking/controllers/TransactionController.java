package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountService accountService;
    @Autowired
    ClientService clientService;

    /*
    TransactionRepository:
    -Buscar una lista de transacciones entre dos fechas pasadas por parametro
    -Buscar una lista de transacciones en las cuales el monto se mayor a x monto pasado como primer parametro,
     y menor a x monto  pasado como segundo parametro
    -Buscar una lista de transacciones por type
    */

    @RequestMapping("/transactions/bydate/{fechadesde}/{fechahasta}")
    List<TransactionDTO> getByDateBetween(@PathVariable String fechadesde, @PathVariable String fechahasta) {
        return transactionRepository.findByDateBetween(LocalDateTime.parse(fechadesde), LocalDateTime.parse(fechahasta)).
                stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/transactions/amountbetween/{amount1}/{amount2}")
    List<TransactionDTO> getByAmountBetween(@PathVariable Double amount1, @PathVariable Double amount2) {
        return transactionRepository.findByAmountGreaterThanAndAndAmountLessThan(amount1,amount2).stream().
                map(TransactionDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/transactions/bytype/{type}")
    List<TransactionDTO> getByType(@PathVariable TransactionType type) {
        return transactionRepository.findByType(type).stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    @Transactional
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

        /*
        * Verificar que exista la cuenta de destino
Verificar que la cuenta de origen tenga el monto disponible.
Se deben crear dos transacciones, una con el tipo de transacción “DEBIT” asociada a la cuenta de origen y la otra con el tipo de transacción “CREDIT” asociada a la cuenta de destino.
A la cuenta de origen se le restará el monto indicado en la petición y a la cuenta de destino se le sumará el mismo monto.
        * */



    }

}
