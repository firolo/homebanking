package com.mindhub.homebanking.dtos;

public class CardPayApplicationDTO {
    private String cardnumber;
    private int cvv;
    private Double amount;
    private String descripcion;

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CardPayApplicationDTO() {}

    public CardPayApplicationDTO(String cardnumber, int cvv, Double amount, String descripcion) {
        this.cardnumber = cardnumber;
        this.cvv = cvv;
        this.amount = amount;
        this.descripcion = descripcion;
    }

}
