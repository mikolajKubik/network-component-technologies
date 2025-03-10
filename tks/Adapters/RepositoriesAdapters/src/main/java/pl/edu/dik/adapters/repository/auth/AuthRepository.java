package pl.edu.dik.adapters.repository.auth;

import pl.edu.dik.adapters.exception.DuplicatedKeyRepositoryException;
import pl.edu.dik.adapters.model.account.AccountEnt;

import java.util.Optional;

public interface AuthRepository {

    AccountEnt save(AccountEnt object) throws DuplicatedKeyRepositoryException;

    Optional<AccountEnt> findByLogin(String login);

    boolean update(AccountEnt account);
}
