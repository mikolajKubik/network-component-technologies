package pl.edu.dik.rest.model.auth;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter @Setter
public class LoginRequest {
    @Size(min = 4, max = 15, message = "Invalid login format")
    private String login;

    @Size(min = 4, max = 20)
    private String password;
}