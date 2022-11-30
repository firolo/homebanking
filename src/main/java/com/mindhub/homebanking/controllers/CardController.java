package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardApplicationDTO;
import com.mindhub.homebanking.dtos.CardPayApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    ClientService clientService;
    @Autowired
    CardService cardService;
    @Autowired
    AccountService accountService;

    @PostMapping("/clients/current/cards")
    ResponseEntity<Object> addCard(Authentication authentication, @RequestParam String cardType, @RequestParam String cardColor) {
        Client client = clientService.findByEmail(authentication.getName());
        if(client.getCards().stream().filter(card -> card.getType().toString().equals(cardType)).count() >= 3) {
            return new ResponseEntity<>("It is allowed a maximum of three cards per client", HttpStatus.FORBIDDEN);
        }

        cardService.create(client,cardType, cardColor);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/cards")
    ResponseEntity<Object> deleteCard(Authentication authentication, @RequestBody CardApplicationDTO card) {
        cardService.delete(card.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("cards/pay")
    ResponseEntity<Object> addPay(Authentication authentication, @RequestBody CardPayApplicationDTO cardPayApplicationDTO) {
        /*
        *  Se debe crear una transacción que indique el débito a una de las cuentas
        *  con la descripción de la operación, (puede ser la primera asociada),
        *  además se debe actualizar la cuenta para restar el monto.
        *  El servicio debe verificar que los datos sean correctos, que la tarjeta
        *  no se encuentre vencida, también debe verificar que se cuente con el monto
        *  en una de las cuentas, debe retornar un mensaje distinto por cada error.
        *  La operación debe ser transaccional.
        * */

        if ((cardPayApplicationDTO.getAmount() == null) || (cardPayApplicationDTO.getCvv() == 0) ||
                cardPayApplicationDTO.getCardnumber().isEmpty() || cardPayApplicationDTO.getDescripcion().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        /* verificar estado de tarjeta */
        Card card = cardService.getCard(cardPayApplicationDTO.getCardnumber());
        if(card == null)
            return new ResponseEntity<>("credit card does not exists", HttpStatus.FORBIDDEN);
        if(card.getCvv() != cardPayApplicationDTO.getCvv())
            return new ResponseEntity<>("invalid cvv", HttpStatus.FORBIDDEN);
        if(card.getThruDate().isBefore(LocalDateTime.now()))
            return new ResponseEntity<>("expired card", HttpStatus.FORBIDDEN);

        List<AccountDTO> accounts = clientService.getCurrentClientAccounts(authentication);
        Set<AccountDTO> accountsWithBalance = accounts.stream().
                filter( accountDTO -> {return accountDTO.getBalance()> cardPayApplicationDTO.getAmount();}).collect(Collectors.toSet());
        if(accountsWithBalance.isEmpty())
            return new ResponseEntity<>("there is not an account with enough money", HttpStatus.FORBIDDEN);

        Account account = accountService.accountByNumber(accountsWithBalance.iterator().next().getNumber());
        cardService.createPay(cardPayApplicationDTO.getCardnumber(), cardPayApplicationDTO.getCvv(),
                                cardPayApplicationDTO.getAmount()*-1, cardPayApplicationDTO.getDescripcion(), account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
