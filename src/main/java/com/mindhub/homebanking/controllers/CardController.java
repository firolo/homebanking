package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @PostMapping("/clients/current/cards")
    ResponseEntity<Object> addCard(Authentication authentication, @RequestParam String cardType, @RequestParam String cardColor) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if(client.getCards().stream().filter(card -> card.getType().toString().equals(cardType)).count() >= 3) {
            return new ResponseEntity<>("It is allowed a maximum of three cards per client", HttpStatus.FORBIDDEN);
        }

        String randomCardNumber =   String.valueOf((int) (Math.random() * (9999 - 1000) + 1000))+"-"+
                                    String.valueOf((int) (Math.random() * (9999 - 1000) + 1000))+"-"+
                                    String.valueOf((int) (Math.random() * (9999 - 1000) + 1000))+"-"+
                                    String.valueOf((int) (Math.random() * (9999 - 1000) + 1000));
        cardRepository.save(new Card(client.getFirstName()+client.getLastName(), CardType.valueOf(cardType),
                CardColor.valueOf(cardColor), randomCardNumber,
                (int) (Math.random() * (999 - 1) + 1), LocalDateTime.now(),
                LocalDateTime.now().plusYears(5), client));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
