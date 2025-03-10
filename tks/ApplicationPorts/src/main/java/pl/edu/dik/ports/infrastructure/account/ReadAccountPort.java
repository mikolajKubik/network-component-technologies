package pl.edu.dik.ports.infrastructure.account;

import pl.edu.dik.domain.model.account.Account;

import java.util.List;
import java.util.Optional;

public interface ReadAccountPort {

    Optional<Account> findById(Object id);

    List<Account> findAll();

    Optional<Account> findByLogin(String login);

    List<Account> findByMatchingLogin(String loginSubstring);
}
