package pl.edu.dik.application.services.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pl.edu.dik.domain.model.account.Account;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.dik.domain.model.account.Role;
import pl.edu.dik.ports.exception.business.AccountNotFoundException;
import pl.edu.dik.ports.exception.business.DuplicatedKeyException;
import pl.edu.dik.ports.exception.business.IncorrectPasswordException;
import pl.edu.dik.ports.infrastructure.auth.CreateAuthPort;
import pl.edu.dik.ports.infrastructure.auth.ReadAuthPort;
import pl.edu.dik.ports.infrastructure.auth.UpdateAuthPort;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceMockTest {

    @Mock
    private CreateAuthPort createAuthPort;

    @Mock
    private ReadAuthPort readAuthPort;

    @Mock
    private UpdateAuthPort updateAuthPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @SneakyThrows
    @Test
    void register() {
        Account account = new Account();
        account.setPassword("plainPassword");
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(createAuthPort.save(any(Account.class))).thenReturn(account);

        Account result = authService.register(account);

        assertThat(result)
                .extracting(Account::getPassword, Account::isEnable, Account::getRole)
                .containsExactly("encodedPassword", true, Role.CLIENT);

        verify(createAuthPort, times(1)).save(any(Account.class));
    }

    @SneakyThrows
    @Test
    void me() {
        Account account = new Account();
        when(readAuthPort.findByLogin("userLogin")).thenReturn(Optional.of(account));

        Account result = authService.me("userLogin");

        assertEquals(account, result);
        verify(readAuthPort, times(1)).findByLogin("userLogin");
    }

    @Test
    void meAccountNotFound() {
        when(readAuthPort.findByLogin("nonexistentUser")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> authService.me("nonexistentUser"));
        verify(readAuthPort, times(1)).findByLogin("nonexistentUser");
    }

    @SneakyThrows
    @Test
    void resetPassword() {
        Account account = new Account();
        account.setPassword("encodedOldPassword");
        when(readAuthPort.findByLogin("userLogin")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        String result = authService.resetPassword("userLogin", "oldPassword", "newPassword");

        assertEquals("Password reset successfully.", result);
        verify(updateAuthPort, times(1)).update(account);
    }

    @Test
    void resetPasswordAccountNotFound() {
        when(readAuthPort.findByLogin("nonexistentUser")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> authService.resetPassword("nonexistentUser", "oldPassword", "newPassword"));
    }

    @Test
    void resetPasswordIncorrectOldPassword() {
        Account account = new Account();
        account.setPassword("encodedOldPassword");
        when(readAuthPort.findByLogin("userLogin")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("wrongOldPassword", "encodedOldPassword")).thenReturn(false);

        assertThrows(IncorrectPasswordException.class, () -> authService.resetPassword("userLogin", "wrongOldPassword", "newPassword"));
    }
}