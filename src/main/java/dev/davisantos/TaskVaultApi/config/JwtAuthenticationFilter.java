package dev.davisantos.TaskVaultApi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Get the authorization header from request
        String authorizationHeader = request.getHeader("Authorization");

        // Verify if the header is not empty, and if begins with a bearer (Like every JWT token)
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {

            // Collect just the token, ignoring the first 7 letters (the bearer that begins in the header)
            String token = authorizationHeader.substring(7);

            //Use our tokenProvider to validate the token
            if (tokenProvider.isTokenValid(token)) {
                String username = tokenProvider.getUsername(token); // Collect the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Get the user in database, using the username

                //Creating a Token to insert in Spring Security Context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // Use all user infos like a principal (That is the identity of user)
                        null, // Can be null, because at this moment we already trust in this token. But it can be the password
                        userDetails.getAuthorities()
                );

                // Setting in Spring Security Context, the token that we created
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //End the filter logic, and begin the next step in to application
        filterChain.doFilter(request, response);
    }

}
