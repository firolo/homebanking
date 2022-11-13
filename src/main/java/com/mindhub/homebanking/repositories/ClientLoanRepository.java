package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {

/*    ClientLoanRepository:
    -Buscar una lista de clientLoan por cliente
    -Buscar una lista de ClientLoan que sean mayores a x monto pasado por parametro
    -Buscar una lista de ClientLoan por cliente que en cual sus balances sean menores a x monto pasado por parametro
*/
    @Query(value = "SELECT cl.* FROM client_loan cl WHERE cl.client_id = ?1", nativeQuery = true)
    List<ClientLoan> findByClientId(long client);

    List<ClientLoan> findByAmountGreaterThan(double amount);

    @Query(value = "SELECT DISTINCT cl.* FROM client_loan cl, account a " +
            "WHERE cl.client_id = a.owner_id " +
            " AND a.balance < ?2 " +
            " AND cl.client_id = ?1", nativeQuery = true)
    List<ClientLoan> findByClientAndAmountGreaterThan(long client, double amount);


}
