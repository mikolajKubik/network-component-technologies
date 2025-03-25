package pl.edu.dik.rest.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.ports._interface.AccountService;
import pl.edu.dik.ports.exception.business.AccountNotFoundException;
import pl.edu.dik.rest.exception.AppExceptionHandler;
import pl.edu.dik.rest.model.auth.AccountResponse;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.UUID;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AccountControllerMockTest {
    @Mock
    private AccountService accountService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    private UUID accountId;
    private Account account;
    private AccountResponse accountResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).setControllerAdvice(new AppExceptionHandler()).build();

        accountId = UUID.randomUUID();
        account = new Account();
        account.setId(accountId);
        account.setLogin("testLogin");

        accountResponse = new AccountResponse();
        accountResponse.setId(accountId.toString());
        accountResponse.setLogin("testLogin");
        accountResponse.setEnable(true);
        accountResponse.setFirstName("Test");
        accountResponse.setLastName("User");
    }

    @SneakyThrows
    @Test
    void findAll() {
        when(accountService.findAllAccounts()).thenReturn(List.of(account));
        when(modelMapper.map(any(Account.class), any())).thenReturn(accountResponse);

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(accountId.toString()))
                .andExpect(jsonPath("$[0].login").value("testLogin"));
    }

    @SneakyThrows
    @Test
    void findById() {
        when(accountService.findAccountById(accountId)).thenReturn(account);
        when(modelMapper.map(any(Account.class), any())).thenReturn(accountResponse);

        mockMvc.perform(get("/api/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.login").value("testLogin"));
    }

    @SneakyThrows
    @Test
    void findByIdNotFound() {
        when(accountService.findAccountById(accountId)).thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(get("/api/accounts/{id}", accountId))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void findByLogin() {
        when(accountService.findByLogin("testLogin")).thenReturn(account);
        when(modelMapper.map(any(Account.class), any())).thenReturn(accountResponse);

        mockMvc.perform(get("/api/accounts/by-login").param("login", "testLogin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.login").value("testLogin"));
    }

    @SneakyThrows
    @Test
    void findByLoginNotFound() {
        when(accountService.findByLogin("nonExistingLogin")).thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(get("/api/accounts/by-login").param("login", "nonExistingLogin"))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void findByMatchingLogin() {
        when(accountService.findByMatchingLogin("testLogin")).thenReturn(List.of(account));
        when(modelMapper.map(any(Account.class), any())).thenReturn(accountResponse);

        mockMvc.perform(get("/api/accounts/search").param("regex", "testLogin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(accountId.toString()))
                .andExpect(jsonPath("$[0].login").value("testLogin"));
    }

    @SneakyThrows
    @Test
    void toggleClientActiveStatus() {
        when(accountService.toggleUserActiveStatus(accountId, true)).thenReturn(account);
        when(modelMapper.map(any(Account.class), any())).thenReturn(accountResponse);

        mockMvc.perform(patch("/api/accounts/{id}/toggle-status", accountId)
                        .param("isActive", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.login").value("testLogin"));
    }

    @SneakyThrows
    @Test
    void toggleClientActiveStatusNotFound() {
        when(accountService.toggleUserActiveStatus(accountId, true)).thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(patch("/api/accounts/{id}/toggle-status", accountId)
                        .param("isActive", "true"))
                .andExpect(status().isNotFound());
    }

}
