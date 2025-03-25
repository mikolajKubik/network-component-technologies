package pl.edu.dik.application.services.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.ports.exception.business.AccountNotFoundException;
import pl.edu.dik.ports.infrastructure.account.ReadAccountPort;
import pl.edu.dik.ports.infrastructure.account.UpdateAccountPort;
import pl.edu.dik.ports._interface.AccountService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ReadAccountPort readAccountPort;
    private final UpdateAccountPort updateAccountPort;

    public Account findAccountById(UUID id) throws AccountNotFoundException {
        return readAccountPort.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found"));
    }

    public List<Account> findAllAccounts() {
        return readAccountPort.findAll();
    }

    public Account updateAccount(Account account) throws AccountNotFoundException {
        Account foundAccount = readAccountPort.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + account.getId() + " not found"));
        if (!account.getLogin().equals(foundAccount.getLogin())) {
            foundAccount.setLogin(account.getLogin());
        }
        if (!account.getPassword().equals(foundAccount.getPassword())) {
            foundAccount.setPassword(account.getPassword());
        }
        return updateAccountPort.update(foundAccount);
    }

    public Account findByLogin(String login) throws AccountNotFoundException {
            return readAccountPort.findByLogin(login)
                    .orElseThrow(() -> new AccountNotFoundException("Account with login " + login + " not found"));

    }

    public List<Account> findByMatchingLogin(String regex) {
        return readAccountPort.findByMatchingLogin(regex);
    }

    public Account toggleUserActiveStatus(UUID id, boolean isActive) throws AccountNotFoundException {
        Account account = readAccountPort.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found"));

        account.setEnable(isActive);
        return updateAccountPort.update(account);
    }
}
