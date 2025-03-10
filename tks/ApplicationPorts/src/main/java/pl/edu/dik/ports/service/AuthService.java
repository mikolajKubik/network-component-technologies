package pl.edu.dik.ports.service;

import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.domain.model.account.Role;

public interface AuthService {

    Account register(Account account);

    Account me(String login);

    String resetPassword(String login, String oldPassword, String newPassword);
}
