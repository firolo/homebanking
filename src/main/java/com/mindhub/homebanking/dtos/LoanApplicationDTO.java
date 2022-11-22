package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private String name;
    private Double amount;
    private Integer payments;
    private String accountNumber;

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public LoanApplicationDTO(String name, Double amount, Integer payments, String accountNumber) {
        this.name = name;
        this.amount = amount;
        this.payments = payments;
        this.accountNumber = accountNumber;
    }
}
