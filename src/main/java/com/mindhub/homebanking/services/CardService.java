package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

public interface CardService {
    public Card create(Client client, String cardType, String cardColor);
    public void delete(Long id);
    Card getCard(String cardnumber);

    void createPay(String cardnumber, int cvv, Double amount, String descripcion, Account account);
}
