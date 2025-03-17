package pl.edu.dik.ports.infrastructure.auth;

import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.ports.exception.business.DuplicatedKeyException;

public interface CreateAuthPort {

    Account save(Account object) throws DuplicatedKeyException;
}
