//package pl.edu.dik.application.services.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import pl.edu.dik.tks.exception.auth.AccountNotFoundException;
//import pl.edu.dik.tks.model.account.Account;
//import pl.edu.dik.tks.repository.account.AccountRepository;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class AccountService {
//    private final AccountRepository accountRepository;
//
//    public Account findAccountById(Object id) throws AccountNotFoundException {
//        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found"));
//    }
//
//    public List<Account> findAllAccounts() {
//        return accountRepository.findAll();
//    }
//
//    public Account updateAccount(Account account) throws AccountNotFoundException {
//        Account foundAccount = accountRepository.findById(account.getId())
//                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + account.getId() + " not found"));
//        if (!account.getLogin().equals(foundAccount.getLogin())) {
//            foundAccount.setLogin(account.getLogin());
//        }
//        if (!account.getPassword().equals(foundAccount.getPassword())) {
//            foundAccount.setPassword(account.getPassword());
//        }
//        return accountRepository.update(foundAccount);
//    }
//
//    public Account findByLogin(String login) throws AccountNotFoundException {
//            return accountRepository.findByLogin(login)
//                    .orElseThrow(() -> new AccountNotFoundException("Account with login " + login + " not found"));
//
//    }
//
//    public List<Account> findByMatchingLogin(String regex) {
//        return accountRepository.findByMatchingLogin(regex);
//    }
//
//    public Account toggleUserActiveStatus(UUID id, boolean isActive) throws AccountNotFoundException {
//        Account account = findAccountById(id);
//        account.setEnable(isActive);
//        return accountRepository.update(account);
//    }
//}
