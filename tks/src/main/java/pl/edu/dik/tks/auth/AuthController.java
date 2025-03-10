package pl.edu.dik.tks.auth;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import pl.edu.dik.tks.auth.dto.*;
import pl.edu.dik.tks.exception.auth.AccountNotFoundException;
import pl.edu.dik.tks.exception.auth.DuplicatedKeyException;
import pl.edu.dik.tks.exception.auth.IncorrectPasswordException;
import pl.edu.dik.tks.model.account.Account;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ModelMapper modelMapper;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) throws
            DuplicatedKeyException {
        Account account = modelMapper.map(registerRequest, Account.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(authService.register(account), RegisterResponse.class));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

        String token = tokenService.generateToken(authentication);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponse> me() throws
            AccountNotFoundException {
        return ResponseEntity.ok(
                modelMapper.map(
                        authService.me(
                                SecurityContextHolder.getContext().getAuthentication().getName()),
                        AccountResponse.class));

    }

    @PatchMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) throws
            AccountNotFoundException,
            IncorrectPasswordException {
        String message = authService.resetPassword(SecurityContextHolder.getContext().getAuthentication().getName(), resetPasswordRequest.getCurrentPassword(), resetPasswordRequest.getNewPassword());
        return ResponseEntity.ok(new ResetPasswordResponse(message));
    }
}
