package pl.edu.dik.domain.model.account;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account implements UserDetails {

    private UUID id;

    private String firstName;

    private String lastName;

    private Role role;

    private boolean isEnable;

    private String login;

    private String password;

    private int rentalCount;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.toString());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }

}
