package pl.edu.dik.tks.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.edu.dik.tks.model.account.Account;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class AuthUserDetails implements UserDetails {
    private final Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + account.getRole().toString());
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getLogin();
    }

    @Override
    public boolean isEnabled() {
        return account.isEnable();
    }
}

