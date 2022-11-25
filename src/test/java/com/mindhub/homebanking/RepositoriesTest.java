package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.utils.PassEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.hamcrest.Matchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    public void existLoans() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existClient() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("firstName",is("Melba"))));
    }

    @Test
    public void existLoansForClient() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("clientLoans",empty())));
    }

    @Test
    public void accountBalanceGreater() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,hasItem(hasProperty("balance",greaterThan(10000d))));
    }

    @Test
    public void accountNumberExists() {
        List<Account> accounts = accountRepository.findByNumber("VIN002");
        assertThat(accounts,notNullValue());
    }

    @Test
    public void cardGoldExists() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,hasItem(hasProperty("color",is(CardColor.GOLD))));
    }

    @Test
    public void cardDebitExists() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,hasItem(hasProperty("type",is(CardType.DEBIT))));
    }

}
