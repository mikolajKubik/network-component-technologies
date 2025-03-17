package pl.edu.dik.application.services.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.dik.domain.model.AuthUserDetails;
import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.ports.infrastructure.auth.ReadAuthPort;
import pl.edu.dik.ports.service.AuthDetailsService;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsServiceImpl implements AuthDetailsService {

    private final ReadAuthPort readAuthPort;

    @Override
    public AuthUserDetails loadUserByUsername(String username) {

        Account account = readAuthPort.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        return new AuthUserDetails(account);

    }
}
