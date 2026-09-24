package dev.davisantos.TaskVaultApi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF is normally used in stateful applications, in rest API's (Like this) we just disable
                .csrf(AbstractHttpConfigurer::disable)
                // Set the application as a stateless session application
                .sessionManagement( // Manager for sessions in Spring Security
                        session -> session.sessionCreationPolicy( // Set the session creation policy
                        SessionCreationPolicy.STATELESS // Set as stateless creation policy
                ))
                // Setting a handler for exceptions, for all requests that requires authentication
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint( // Set a EntryPoint to handler return
                            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED) // Set the return as a 401 Status Code
                        ).accessDeniedHandler( // Set an access denied handler
                                (request, response, accessDeniedException) -> {
                                    response.setStatus(HttpStatus.FORBIDDEN.value()); // Set the access denied as 403 status code
                                }
                        )
                )
                // Setting the endpoints access rules
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**").permitAll() // Set all endpoints of "auth" as free access
                    .anyRequest().authenticated() // Set any else endpoint as only authenticated can access
                )
                // Set authentication filters before the default security filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                // Disabling default form login
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return  authenticationConfiguration.getAuthenticationManager();
    }
}
