package pl.edu.dik.ports.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthDetailsService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
