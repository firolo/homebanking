package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.Set;

public interface AccountService {
    public Account createAccount(Client client);
    public Set<AccountDTO> getAll();
    public AccountDTO getAccount(long id);
    public List<AccountDTO> getAccountByBalanceGreaterThan(double balance);
    public List<AccountDTO> findByCreationDateBefore(String localdatetime);
    public List<AccountDTO> findByNumber(String number);
    public Account accountByNumber(String number);

}
