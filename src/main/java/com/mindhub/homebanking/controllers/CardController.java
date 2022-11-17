package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
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
    ClientService clientService;
    @Autowired
    CardService cardService;

    @PostMapping("/clients/current/cards")
    ResponseEntity<Object> addCard(Authentication authentication, @RequestParam String cardType, @RequestParam String cardColor) {
        Client client = clientService.findByEmail(authentication.getName());
        if(client.getCards().stream().filter(card -> card.getType().toString().equals(cardType)).count() >= 3) {
            return new ResponseEntity<>("It is allowed a maximum of three cards per client", HttpStatus.FORBIDDEN);
        }

        cardService.create(client,cardType, cardColor);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
