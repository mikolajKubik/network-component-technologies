package pl.edu.dik.rest.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.ports._interface.AccountService;
import pl.edu.dik.ports.exception.business.AccountNotFoundException;
import pl.edu.dik.rest.model.auth.AccountResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final ModelMapper modelMapper;
    private final AccountService accountService;


    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        List<Account> accounts = accountService.findAllAccounts();
        List<AccountResponse> responses = accounts.stream()
                .map(account -> modelMapper.map(account, AccountResponse.class))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable UUID id) throws AccountNotFoundException {
        Account foundAccount = accountService.findAccountById(id);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(foundAccount, AccountResponse.class));
    }

    @GetMapping("/by-login")
    public ResponseEntity<AccountResponse> findByLogin(@RequestParam String login) throws AccountNotFoundException {
        Account foundAccount = accountService.findByLogin(login);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(foundAccount, AccountResponse.class));
    }

    @GetMapping("search")
    public ResponseEntity<List<AccountResponse>> findByMatchingLogin(@RequestParam String regex) {
        List<Account> accounts = accountService.findByMatchingLogin(regex);
        List<AccountResponse> responses = accounts.stream()
                .map(account -> modelMapper.map(account, AccountResponse.class))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<AccountResponse> toggleClientActiveStatus(@PathVariable UUID id, @RequestParam boolean isActive) throws AccountNotFoundException {
        Account client = accountService.toggleUserActiveStatus(id, isActive);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(client, AccountResponse.class));
    }
}
