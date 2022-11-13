package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountService accountService;

    @Override
    public Client createClientPlusAccount(String firstName, String lastName, String email, String passwordEncoder) {
        Client client = new Client(firstName, lastName, email, passwordEncoder);
        clientRepository.save(client);
        accountService.createAccount(client);
        return client;
    }
}
