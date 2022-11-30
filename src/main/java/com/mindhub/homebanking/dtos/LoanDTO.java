package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import java.util.List;

public class LoanDTO {
    private Long id;
    private String name;
    private Double maxAmount;
    private Double percent;

    @ElementCollection
    @Column(name="payment")
    private List<Integer> payments;

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public Double getPercent() {
        return percent;
    }

    public Long getId() {
        return id;
    }

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.percent = loan.getPercent();
    }
}
