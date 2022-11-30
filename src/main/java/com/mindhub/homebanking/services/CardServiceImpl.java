package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionService transactionService;


    @Override
    public Card create(Client client, String cardType, String cardColor) {
        String randomCardNumber = CardUtils.getCardNumber();
        Card card = new Card(client.getFirstName()+client.getLastName(), CardType.valueOf(cardType),
                CardColor.valueOf(cardColor), randomCardNumber,
                CardUtils.getCVV(), LocalDateTime.now(),
                LocalDateTime.now().plusYears(5), client);

        cardRepository.save(card);
        return card;
    }

    @Override
    public void delete(Long id) {
        Card card = cardRepository.findById(id).orElse(null);
        card.setActive(false);
        cardRepository.save(card);
    }

    @Override
    public Card getCard(String cardnumber) {
        return cardRepository.findByNumber(cardnumber);
    }

    @Override
    public void createPay(String cardnumber, int cvv, Double amount, String descripcion, Account account) {
        transactionService.createForLoan(amount,descripcion,account);
    }
}
