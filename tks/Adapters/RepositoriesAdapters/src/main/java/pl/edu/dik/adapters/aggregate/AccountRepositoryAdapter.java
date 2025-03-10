package pl.edu.dik.adapters.aggregate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.dik.adapters.repository.account.AccountRepository;
import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.ports.infrastructure.account.ReadAccountPort;
import pl.edu.dik.ports.infrastructure.account.UpdateAccountPort;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements ReadAccountPort, UpdateAccountPort {

    private final AccountRepository accountRepository;

    @Override
    public Account update(Account updatedAccount) {
        return null;
    }

    @Override
    public Optional<Account> findById(Object id) {
        return Optional.empty();
    }

    @Override
    public List<Account> findAll() {
        return List.of();
    }

    @Override
    public Optional<Account> findByLogin(String login) {
        return Optional.empty();
    }

    @Override
    public List<Account> findByMatchingLogin(String loginSubstring) {
        return List.of();
    }
}
