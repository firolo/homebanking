package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card create(Client client, String cardType, String cardColor) {
        String randomCardNumber =   String.valueOf((int) (Math.random() * (9999 - 1000) + 1000))+"-"+
                String.valueOf((int) (Math.random() * (9999 - 1000) + 1000))+"-"+
                String.valueOf((int) (Math.random() * (9999 - 1000) + 1000))+"-"+
                String.valueOf((int) (Math.random() * (9999 - 1000) + 1000));
        Card card = new Card(client.getFirstName()+client.getLastName(), CardType.valueOf(cardType),
                CardColor.valueOf(cardColor), randomCardNumber,
                (int) (Math.random() * (999 - 1) + 1), LocalDateTime.now(),
                LocalDateTime.now().plusYears(5), client);

        cardRepository.save(card);
        return card;
    }
}
