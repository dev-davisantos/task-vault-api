package dev.davisantos.TaskVaultApi.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class TokenProvider {
    /*
     Class used to learn the process to create a token manually
     So, because of this, I'm commenting every line, for reinforce the learning
     */

    @Value("${jwt.expiration}")
    private Long expirationTime;
    @Value("${jwt.key}")
    private String key;

    //Method used to take the user datas, and call the token builder
    public String generateToken(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal(); //Transform Authentication to UserDetails
        return buildToken(user.getUsername()); // Call Token Builder
    }

    //Method used to create a token
    public String buildToken(String username) {
        Date now = new Date(); //Get time that token was built
        Date expiration = new Date(now.getTime() + expirationTime); //Set the expiration time of this token

        return Jwts.builder()
                .subject(username) // Setting the subject of token as username (or e-mail)
                .issuedAt(now) // Set the time that it was created
                .expiration(expiration) // Set the time that this token will expire
                .signWith(getSigningKey()) // Call the signing key generator, and set in the token
                .compact(); // Just end the process of create a JWT token
    }

    //Method used to generate a Secret Key for the token
    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(key.getBytes()); // Generate a hmacShaKey from bytes of our key (that was got from environment variables)
    }

}
