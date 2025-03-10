package pl.edu.dik.tks.auth.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class LoginRequest {
    private String login;
    private String password;
}