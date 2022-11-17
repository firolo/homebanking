package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;


public interface ClientService {
    public Client createClientPlusAccount(String firstName, String lastName, String email, String passwordEncoder);
    public boolean isAccountFromClient(String email, String account);
    public List<ClientDTO> getAll();
    public ClientDTO getById(long id);
    public List<ClientDTO> getClientByFirstName(String firstname);
    public List<ClientDTO> getClientByFirstNameAndEmail(String firstname, String email);
    public List<ClientDTO> getClientByLastName(String lastname);
    public Client  getCurrentClient(Authentication authentication);
    public List<AccountDTO>  getCurrentClientAccounts(Authentication authentication);

    public Client findByEmail(String email);
}
