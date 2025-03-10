package pl.edu.dik.view.rest.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotEmpty(message = "Current Password is required")
    @Size(min = 3, max = 100, message = "Current password must be between 3 and 30 characters")
    private String currentPassword;

    @NotEmpty(message = "New Password is required")
    @Size(min = 3, max = 100, message = "New password must be between 3 and 30 characters")
    private String newPassword;
}