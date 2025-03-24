package pl.edu.dik.adapters.aggregate;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.edu.dik.adapters.exception.DuplicatedKeyRepositoryException;
import pl.edu.dik.adapters.model.account.AccountEnt;
import pl.edu.dik.adapters.model.account.RoleEnt;
import pl.edu.dik.adapters.repository.auth.AuthRepository;
import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.domain.model.account.Role;
import pl.edu.dik.ports.exception.business.DuplicatedKeyException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthRepositoryAdapterMockTest {

    @Mock
    private AuthRepository authRepository;
    private AuthRepositoryAdapter authRepositoryAdapter;

    private AccountEnt accountEnt;
    private Account account;

    @BeforeEach
    void setUp() {
        authRepositoryAdapter = new AuthRepositoryAdapter(authRepository, new ModelMapper());
        UUID accountId = UUID.randomUUID();
        accountEnt = new AccountEnt(accountId, "Test", "User", RoleEnt.CLIENT, true, "login", "password", 0);
        account = new Account(accountId, "Test", "User", Role.CLIENT, true, "login", "password", 0);
    }

    @SneakyThrows
    @Test
    void saveTest() {
        when(authRepository.save(accountEnt)).thenReturn(accountEnt);

        Account result = authRepositoryAdapter.save(account);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(accountEnt);

        verify(authRepository).save(accountEnt);
    }

    @SneakyThrows
    @Test
    void saveDuplicatedKeyExceptionTest() {
        when(authRepository.save(accountEnt)).thenThrow(new DuplicatedKeyRepositoryException("Duplicated key"));

        DuplicatedKeyException exception = assertThrows(DuplicatedKeyException.class, () -> {
            authRepositoryAdapter.save(account);
        });
        assertEquals("Duplicated key", exception.getMessage());

        verify(authRepository).save(accountEnt);
    }

    @Test
    void findByLoginTest() {
        when(authRepository.findByLogin("login")).thenReturn(Optional.ofNullable(accountEnt));

        Optional<Account> result = authRepositoryAdapter.findByLogin("login");

        assertThat(result)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(accountEnt);

        verify(authRepository).findByLogin("login");
    }

    @Test
    void updateTest() {
        when(authRepository.update(accountEnt)).thenReturn(true);

        boolean result = authRepositoryAdapter.update(account);

        assertTrue(result);

        verify(authRepository).update(accountEnt);
    }
}