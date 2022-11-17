package com.mindhub.homebanking.services;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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
    @Override
    public boolean isAccountFromClient(String email, String account) {
        Client client = clientRepository.findByEmail(email);
        return client.getAccounts().stream().filter(account1 -> account1.getNumber().equals(account)).collect(Collectors.toSet()).isEmpty();
    }
    @Override
    public List<ClientDTO> getAll() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }
    @Override
    public ClientDTO getById(long id) {
        return  clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }
    @Override
    public List<ClientDTO> getClientByFirstName(String firstname){
        return clientRepository.findByFirstName(firstname).stream().map(ClientDTO::new).collect(Collectors.toList());
    }
    @Override
    public List<ClientDTO> getClientByFirstNameAndEmail(String firstname,String email) {
        return clientRepository.findByFirstNameAndEmail(firstname,email).stream().map(ClientDTO::new).collect(Collectors.toList());
    }
    @Override
    public List<ClientDTO> getClientByLastName(String lastname){
        return clientRepository.findByLastName(lastname).stream().map(ClientDTO::new).collect(Collectors.toList());
    }
    @Override
    public Client getCurrentClient(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName());
    }
    @Override
    public List<AccountDTO>  getCurrentClientAccounts(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }


}
