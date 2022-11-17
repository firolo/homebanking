package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public List<ClientDTO> getAll(){
        return clientService.getAll();
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable long id){
        return clientService.getById(id);
    }

    @RequestMapping("/clients/firstname/{firstname}")
    public List<ClientDTO> getClientByFirstName(@PathVariable String firstname){
        return clientService.getClientByFirstName(firstname);
    }

    @RequestMapping("/clients/{firstname}/{email}")
    public List<ClientDTO> getClientByFirstNameAndEmail(@PathVariable String firstname, @PathVariable String email) {
        return clientService.getClientByFirstNameAndEmail(firstname,email);
    }

    @RequestMapping("/clients/lastname/{lastname}")
    public List<ClientDTO> getClientByLastName(@PathVariable String lastname){
        return clientService.getClientByLastName(lastname);
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email)!= null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        clientService.createClientPlusAccount(firstName, lastName, email, passwordEncoder.encode(password));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/clients/current", method = RequestMethod.GET)
    public ClientDTO  getCurrentClient(Authentication authentication) {
        return new ClientDTO(clientService.getCurrentClient(authentication));
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.GET)
    public List<AccountDTO>  getCurrentClientAccounts(Authentication authentication) {
        return clientService.getCurrentClientAccounts(authentication);
    }


}
