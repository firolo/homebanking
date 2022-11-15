package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;



public interface ClientService {
    public Client createClientPlusAccount(String firstName, String lastName, String email, String passwordEncoder);
    public boolean isAccountFromClient(String email, String account);
}
