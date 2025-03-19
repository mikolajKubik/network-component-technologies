package pl.edu.dik.ports.infrastructure.account;

import pl.edu.dik.domain.model.account.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadAccountPort {

    Optional<Account> findById(UUID id);

    List<Account> findAll();

    Optional<Account> findByLogin(String login);

    List<Account> findByMatchingLogin(String loginSubstring);
}
