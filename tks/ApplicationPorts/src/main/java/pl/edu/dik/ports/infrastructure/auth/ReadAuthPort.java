package pl.edu.dik.ports.infrastructure.auth;

import pl.edu.dik.domain.model.account.Account;

import java.util.Optional;

public interface ReadAuthPort {

    Optional<Account> findByLogin(String login);
}
