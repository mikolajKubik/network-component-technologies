package pl.edu.dik.ports._interface;

import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.ports.exception.business.AccountNotFoundException;
import pl.edu.dik.ports.exception.business.DuplicatedKeyException;
import pl.edu.dik.ports.exception.business.IncorrectPasswordException;

public interface AuthService {

    Account register(Account account) throws DuplicatedKeyException;

    Account me(String login) throws AccountNotFoundException;

    String resetPassword(String login, String oldPassword, String newPassword) throws AccountNotFoundException, IncorrectPasswordException;
}
