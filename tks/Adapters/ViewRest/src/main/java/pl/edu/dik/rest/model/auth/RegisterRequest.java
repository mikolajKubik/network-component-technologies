package pl.edu.dik.rest.model.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    @Size(min = 4, max = 20, message = "Login must be between 4 and 20 characters")
    private String login;

    @NotNull
    @NotEmpty
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    private String password;
}