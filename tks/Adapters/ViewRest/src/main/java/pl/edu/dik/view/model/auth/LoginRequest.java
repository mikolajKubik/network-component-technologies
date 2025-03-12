package pl.edu.dik.view.model.auth;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class LoginRequest {
    private String login;
    private String password;
}