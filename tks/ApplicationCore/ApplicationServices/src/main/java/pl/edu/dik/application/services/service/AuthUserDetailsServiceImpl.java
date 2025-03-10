package pl.edu.dik.application.services.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.edu.dik.application.services.exception.business.UsernameNotFoundException;
import pl.edu.dik.domain.model.AuthUserDetails;
import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.ports.infrastructure.auth.ReadAuthPort;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    private final ReadAuthPort readAuthPort;

    @Override
    public AuthUserDetails loadUserByUsername(String username) {
        try {
            Account account = readAuthPort.findByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            return new AuthUserDetails(account);

        } catch (Exception e) {
            throw e;
        }
    }
}
