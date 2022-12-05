package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers


			Client client1 = new Client("Melba", "Lorenzo","melba@galletitas.com.ar",passwordEncoder.encode("123"));
			Client client2 = new Client("Ronaldo", "Da Lima","r9@galletitas.com.ar",passwordEncoder.encode("Incacola"));
			Client client3 = new Client("admin", "admin","admin",passwordEncoder.encode("admin"));

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			Account acc1 = new Account("VIN001", LocalDateTime.now(), 0.0, client1, AccountType.CAJA_AHORRO);
			Account acc2 = new Account("VIN002", LocalDateTime.now().plusDays(1),0.0, client1, AccountType.CAJA_AHORRO);
			Account acc3 = new Account("VIN003", LocalDateTime.now().plusDays(1),10000.0, client2, AccountType.CUENTA_CORRIENTE);

			Transaction trn1 = new Transaction(TransactionType.CREDITO,752.15,"Cobro MercadoPago",LocalDateTime.now(),acc1);
			Transaction trn2 = new Transaction(TransactionType.DEBITO,615.59,"Pago almacén",LocalDateTime.now(),acc1);
			Transaction trn3 = new Transaction(TransactionType.CREDITO,10000.2,"Transferencia entrante",LocalDateTime.now(),acc2);

			Loan loan1 = new Loan("Hipotecario",500000d,List.of(12,24,36,48,60),20d);
			Loan loan2 = new Loan("Personal",100000d,List.of(6,12,24),20d);
			Loan loan3 = new Loan("Automotriz",300000d, List.of(6,12,24,36),20d);

			ClientLoan clientLoan1 = new ClientLoan(client1, loan1, 400000d, 60);
			ClientLoan clientLoan2 = new ClientLoan(client1, loan2, 50000d, 12);
			ClientLoan clientLoan3 = new ClientLoan(client2, loan2, 100000d, 24);
			ClientLoan clientLoan4 = new ClientLoan(client2, loan3, 200000d, 36);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			accountRepository.save(acc1);
			accountRepository.save(acc2);
			accountRepository.save(acc3);

			transactionRepository.save(trn1);
			transactionRepository.save(trn2);
			transactionRepository.save(trn3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(client1.getFirstName().concat(" ").concat(client1.getLastName()),CardType.DEBIT, CardColor.GOLD, "1234-5645-1287-1231",123, LocalDateTime.now(), LocalDateTime.now().plusYears(5) ,client1);
			Card card2 = new Card(client1.getFirstName().concat(" ").concat(client1.getLastName()),CardType.DEBIT, CardColor.TITANIUM, "9999-8888-1787-1867",456, LocalDateTime.now(), LocalDateTime.now().plusYears(5) ,client1);
			Card card3 = new Card(client2.getFirstName().concat(" ").concat(client2.getLastName()),CardType.CREDIT, CardColor.SILVER, "1627-4558-8134-4605",789, LocalDateTime.now(), LocalDateTime.now().plusYears(5) ,client2);
			//Card card4 = new Card(client1.getFirstName().concat(" ").concat(client2.getLastName()),CardType.CREDIT, CardColor.SILVER, "1627-4558-8134-4605",789, LocalDateTime.now(), LocalDateTime.now().plusYears(5) ,client1);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			//cardRepository.save(card4);
		};
	}

}
