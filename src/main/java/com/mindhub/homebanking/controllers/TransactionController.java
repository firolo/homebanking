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
@RequestMapping("/api/transactions")
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

    @RequestMapping("/bydate/{fechadesde}/{fechahasta}")
    List<TransactionDTO> getByDateBetween(@PathVariable String fechaDesde, @PathVariable String fechaHasta) {
        return transactionRepository.findByDateBetween(LocalDateTime.parse(fechaDesde), LocalDateTime.parse(fechaHasta)).
                stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/amountbetween/{amount1}/{amount2}")
    List<TransactionDTO> getByAmountBetween(@PathVariable Double amount1, @PathVariable Double amount2) {
        return transactionRepository.findByAmountGreaterThanAndAndAmountLessThan(amount1,amount2).stream().
                map(TransactionDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/bytype/{type}")
    List<TransactionDTO> getByType(@PathVariable TransactionType type) {
        return transactionRepository.findByType(type).stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/add")
    ResponseEntity<Object> createTransaction(Authentication authentication, @RequestParam Double monto, @RequestParam String desc,
                                             @RequestParam String ctaOrigen, @RequestParam String ctaDest ) {


        if(monto.toString().isEmpty() || desc.isEmpty() || ctaOrigen.isEmpty() || ctaDest.isEmpty()) {
            return new ResponseEntity<>("Incomplete parameters", HttpStatus.FORBIDDEN);
        }

        if(ctaOrigen.equals(ctaDest)) {
            return new ResponseEntity<>("Accounts must be different", HttpStatus.FORBIDDEN);
        }

        Account account1 = accountService.accountByNumber(ctaOrigen);
        if(account1==null) {
            return new ResponseEntity<>("Origin account does not exist", HttpStatus.FORBIDDEN);
        }

        if(clientService.isAccountFromClient(authentication.getName().toString(), ctaOrigen)) {
            return new ResponseEntity<>("The origin account does not belongs to client", HttpStatus.FORBIDDEN);
        }

        Account account2 = accountService.accountByNumber(ctaDest);
        if(account2==null) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }

        if(account1.getBalance() < monto) {
            return new ResponseEntity<>("Origen account has not got enough money", HttpStatus.FORBIDDEN);
        }

        transactionService.create(TransactionType.DEBITO, monto, desc, LocalDateTime.now(), account1);
        transactionService.create(TransactionType.CREDITO, monto, desc, LocalDateTime.now(), account2);

        return new ResponseEntity<>(HttpStatus.CREATED);

        /*
        * Verificar que exista la cuenta de destino
Verificar que la cuenta de origen tenga el monto disponible.
Se deben crear dos transacciones, una con el tipo de transacción “DEBIT” asociada a la cuenta de origen y la otra con el tipo de transacción “CREDIT” asociada a la cuenta de destino.
A la cuenta de origen se le restará el monto indicado en la petición y a la cuenta de destino se le sumará el mismo monto.
        * */



    }

}
