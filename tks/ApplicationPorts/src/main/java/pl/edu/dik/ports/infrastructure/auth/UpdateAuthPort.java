package pl.edu.dik.ports.infrastructure.auth;

import pl.edu.dik.domain.model.account.Account;

public interface UpdateAuthPort {

    boolean update(Account account);
}
