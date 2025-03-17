package pl.edu.dik.application.services.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.domain.model.account.Role;
import pl.edu.dik.ports.exception.business.AccountNotFoundException;
import pl.edu.dik.ports.exception.business.DuplicatedKeyException;
import pl.edu.dik.ports.exception.business.IncorrectPasswordException;
import pl.edu.dik.ports.infrastructure.auth.CreateAuthPort;
import pl.edu.dik.ports.infrastructure.auth.ReadAuthPort;
import pl.edu.dik.ports.infrastructure.auth.UpdateAuthPort;
import pl.edu.dik.ports._interface.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CreateAuthPort createAuthPort;
    private final ReadAuthPort readAuthPort;
    private final UpdateAuthPort updateAuthPort;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Account register(Account account) throws
            DuplicatedKeyException {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setEnable(true);
        account.setRole(Role.CLIENT);
        return createAuthPort.save(account);
    }


    @Override
    public Account me(String login) throws
            AccountNotFoundException {
        return readAuthPort.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }


    @Override
    public String resetPassword(String login, String oldPassword, String newPassword) throws
            AccountNotFoundException, IncorrectPasswordException {
        Account account = readAuthPort.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }
        account.setPassword(passwordEncoder.encode(newPassword));
        updateAuthPort.update(account);
        return "Password reset successfully.";
    }
}
