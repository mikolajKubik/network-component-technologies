package pl.edu.dik.ports.service;

import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.ports.exception.business.AccountNotFoundException;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    Account findAccountById(UUID id) throws AccountNotFoundException;

    List<Account> findAllAccounts();

    Account updateAccount(Account account) throws AccountNotFoundException;

    Account findByLogin(String login) throws AccountNotFoundException;

    List<Account> findByMatchingLogin(String regex);

    Account toggleUserActiveStatus(UUID id, boolean isActive) throws AccountNotFoundException;
}
