package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public List<ClientDTO> getAll(){
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable long id){

    return  clientRepository.findById(id).map(cliente -> new ClientDTO(cliente)).orElse(null);
    }

    @RequestMapping("/clients/firstname/{firstname}")
    public List<ClientDTO> getClientByFirstName(@PathVariable String firstname){

        return clientRepository.findByFirstName(firstname).stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/clients/{firstname}/{email}")
    public List<ClientDTO> getClientByFirstNameAndEmail(@PathVariable String firstname, @PathVariable String email) {
        return clientRepository.findByFirstNameAndEmail(firstname,email).stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/clients/lastname/{lastname}")
    public List<ClientDTO> getClientByLastName(@PathVariable String lastname){
        return clientRepository.findByLastName(lastname).stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        clientService.createClientPlusAccount(firstName, lastName, email, passwordEncoder.encode(password));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/clients/current", method = RequestMethod.GET)
    public ClientDTO  getCurrentClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }

}
