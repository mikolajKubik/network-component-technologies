package pl.edu.dik.tks.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.dik.tks.exception.auth.AccountNotFoundException;
import pl.edu.dik.tks.exception.auth.DuplicatedKeyException;
import pl.edu.dik.tks.exception.auth.IncorrectPasswordException;
import pl.edu.dik.tks.model.account.Account;
import pl.edu.dik.tks.model.account.Role;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public Account register(Account account) throws
            DuplicatedKeyException {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setEnable(true);
        account.setRole(Role.CLIENT);
        return authRepository.save(account);
    }

    public Account me(String login) throws
            AccountNotFoundException {
        return authRepository.findByLogin(login).orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    public String resetPassword(String login, String oldPassword, String newPassword) throws
            AccountNotFoundException, IncorrectPasswordException {
        Account account = authRepository.findByLogin(login).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }
        account.setPassword(passwordEncoder.encode(newPassword));
        authRepository.update(account);
        return "Password reset successfully.";
    }
}
