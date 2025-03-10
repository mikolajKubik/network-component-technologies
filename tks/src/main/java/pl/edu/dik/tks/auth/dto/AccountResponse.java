package pl.edu.dik.tks.auth.dto;

import lombok.*;

import java.util.UUID;

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
