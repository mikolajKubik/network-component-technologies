package pl.edu.dik.ports.infrastructure.account;

import pl.edu.dik.domain.model.account.Account;

public interface UpdateAccountPort {

    Account update(Account updatedAccount);

}
