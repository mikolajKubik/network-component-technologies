package pl.edu.dik.domain.model.account;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private UUID id;

    private String firstName;

    private String lastName;

    private Role role;

    private boolean isEnable;

    private String login;

    private String password;

    private int rentalCount;

}
