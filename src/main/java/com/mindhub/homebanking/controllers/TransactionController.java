package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;

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

}
