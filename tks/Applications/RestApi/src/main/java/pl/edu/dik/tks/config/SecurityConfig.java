package pl.edu.dik.tks.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import pl.edu.dik.tks.auth.AuthUserDetailsService;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserDetailsService authUserDetailsService;

    @Getter
    @Value("${rsa.private-key}")
    private RSAPrivateKey privateKey;

    @Getter
    @Value("${rsa.public-key}")
    private RSAPublicKey publicKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authenticationProvider(authenticationProvider())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/api/auth/reset-password").authenticated()

                                .requestMatchers(HttpMethod.POST, "/api/games/create").hasAnyRole("CLIENT", "EMPLOYEE") // TODO: remove client role
                                .requestMatchers(HttpMethod.GET, "/api/games/{id}").hasAnyRole("CLIENT", "EMPLOYEE") // TODO: remove client role
                                .requestMatchers(HttpMethod.PUT, "/api/games").hasAnyRole("CLIENT", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/games").hasAnyRole("CLIENT", "EMPLOYEE")
                                .requestMatchers(HttpMethod.DELETE, "/api/games/{id}").hasAnyRole("CLIENT", "EMPLOYEE")

                                .requestMatchers(HttpMethod.GET, "/api/accounts").hasAnyRole("CLIENT", "EMPLOYEE", "ADMIN") // todo: same
                                .requestMatchers(HttpMethod.GET, "/api/accounts/{id}").hasAnyRole("CLIENT", "EMPLOYEE", "ADMIN") // todo: same
                                .requestMatchers(HttpMethod.GET, "/api/accounts/by-login").hasAnyRole("CLIENT", "EMPLOYEE", "ADMIN") // todo: same
                                .requestMatchers(HttpMethod.GET, "/api/accounts/search").hasAnyRole("CLIENT", "EMPLOYEE", "ADMIN") // todo: same
                                .requestMatchers(HttpMethod.PATCH, "/api/accounts/{id}/toggle-status").hasAnyRole("CLIENT", "EMPLOYEE", "ADMIN") // todo: same

                                .requestMatchers(HttpMethod.POST, "/api/rents").hasAnyRole("CLIENT", "EMPLOYEE") // TODO: remove client role
                                .requestMatchers(HttpMethod.GET, "/api/rents/{id}").hasAnyRole("CLIENT", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/rents").hasAnyRole("CLIENT", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/rents/client/").hasAnyRole("CLIENT", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/rents/end/{id}").hasAnyRole("CLIENT", "EMPLOYEE")

                                .requestMatchers(HttpMethod.GET, "/api/inactive-rents/").hasAnyRole("CLIENT", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/inactive-rents").hasAnyRole("CLIENT", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/inactive-rents/client/").hasAnyRole("CLIENT", "EMPLOYEE")

                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {
                            jwt.decoder(jwtDecoder());
                            jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
                        })
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this configuration to all endpoints
        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return authenticationConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(authUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

}
