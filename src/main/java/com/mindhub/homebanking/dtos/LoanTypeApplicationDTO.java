package com.mindhub.homebanking.dtos;

public class LoanTypeApplicationDTO {
    private String name;
    private Double maxAmount;
    private Double percent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public LoanTypeApplicationDTO() {}

    public LoanTypeApplicationDTO(String name, Double maxAmount, Double percent) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.percent = percent;
    }
}
