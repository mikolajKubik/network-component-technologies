package pl.edu.dik.application.services.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.dik.ports.infrastructure.auth.ReadAuthPort;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    private final ReadAuthPort readAuthPort;

    @Override
    public UserDetails loadUserByUsername(String username) {

        return readAuthPort.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

    }
}
