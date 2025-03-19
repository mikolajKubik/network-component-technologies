package pl.edu.dik.rest.model.auth;

import lombok.*;

@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String login;
    private int rentalCount;
    private boolean isEnable;
}
