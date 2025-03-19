package pl.edu.dik.adapters.repository.account;


import pl.edu.dik.adapters.model.account.AccountEnt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    Optional<AccountEnt> findById(UUID id);

    AccountEnt update(AccountEnt updatedAccount);

    List<AccountEnt> findAll();

    Optional<AccountEnt> findByLogin(String login);

    List<AccountEnt> findByMatchingLogin(String loginSubstring);
}

