package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client,Long> {

    List<Client> findByFirstName(String firstName);
    List<Client> findByFirstNameAndEmail(String firstName, String email);
    List<Client> findByLastName(String lastName);
    Client findByEmail(String email);

}
