package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.*;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class LoanController {
    @Autowired
    LoanService loanService;
    @Autowired
    AccountService accountService;
    @Autowired
    ClientService clientService;
    @Autowired
    ClientLoanService clientLoanService;
    @Autowired
    TransactionService transactionService;

    @GetMapping("/api/loans")
    public Set<LoanDTO> getLoans()  {
        return loanService.getLoans();
    }

    @PostMapping("api/loans")
    public ResponseEntity<Object> addLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApp) {

        if (loanApp.getLoanId()==null||loanApp.getToAccountNumber()==null||loanApp.getPayments() == null||loanApp.getAmount() == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.getLoan(loanApp.getLoanId());

        if(loan == null) {
            return new ResponseEntity<>("Invalid loan", HttpStatus.FORBIDDEN);
        }

        if(loanApp.getAmount() > loan.getMaxAmount())
            return new ResponseEntity<>("Amount greater than allow", HttpStatus.FORBIDDEN);

        if(!loan.getPayments().contains(loanApp.getPayments()))
            return new ResponseEntity<>("Invalid payment", HttpStatus.FORBIDDEN);

        Account account= accountService.accountByNumber(loanApp.getToAccountNumber());

        if(account == null)
            return new ResponseEntity<>("Invalid account", HttpStatus.FORBIDDEN);

        List<AccountDTO> clientAccounts = clientService.getCurrentClientAccounts(authentication);

        if(clientAccounts.stream().filter(accountDTO -> accountDTO.getNumber().equals(account.getNumber())).collect(Collectors.toSet()).isEmpty()) {
            return new ResponseEntity<>("Account does not belong to the client", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.getCurrentClient(authentication);

        Double finalAmount = clientLoanService.add(client, loan, loanApp.getAmount(), loanApp.getPayments());

        transactionService.createForLoan(finalAmount, loanApp.getLoanId()+" loan approved",account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("api/loantype")
    public ResponseEntity<Object> addLoanType(Authentication authentication, @RequestBody LoanTypeApplicationDTO loanTypeApp) {
        Loan loan = new Loan(loanTypeApp.getName(), loanTypeApp.getMaxAmount(),List.of(6,12), loanTypeApp.getPercent());
        loanService.createLoanType(loan);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
