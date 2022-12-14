package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.LoanTypeApplicationDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.Set;

public interface LoanService {
    Set<LoanDTO> getLoans();
    boolean loanValid(String name);
    Loan getLoan(Long id);

    void createLoanType(Loan loan);
}
