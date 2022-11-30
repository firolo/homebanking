package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.dtos.LoanTypeApplicationDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    LoanRepository loanRepository;
    @Override
    public Set<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toSet());
    }

    @Override
    public boolean loanValid(String name) {
        Set<Loan> loans = loanRepository.findAll().stream().filter(loan -> loan.getName().equals(name)).collect(Collectors.toSet());
        if (loans.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public Loan getLoan(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public void createLoanType(Loan loan) {
        loanRepository.save(loan);
    }
}
