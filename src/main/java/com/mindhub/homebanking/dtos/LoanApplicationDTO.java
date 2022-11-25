package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private Long loanId;
    private Double amount;
    private Integer payments;
    private String toAccountNumber;

    public Long getLoanId() {
        return loanId;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }


    public LoanApplicationDTO(Long loanId, Double amount, Integer payments, String accountNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = accountNumber;
    }
}
