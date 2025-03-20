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

    private UUID accountId;
    private AccountEnt accountEnt;
    private Account account;

    @BeforeEach
    void setUp() {
        authRepositoryAdapter = new AuthRepositoryAdapter(authRepository, new ModelMapper());
        accountId = UUID.randomUUID();
        accountEnt = new AccountEnt("Test", "User", RoleEnt.CLIENT, true, "login", "password", 0);
        accountEnt.setId(accountId);
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

    }

    @Test
    void findByLogin() {
    }

    @Test
    void update() {
    }
}