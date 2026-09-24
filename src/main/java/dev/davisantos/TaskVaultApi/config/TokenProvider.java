package dev.davisantos.TaskVaultApi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenProvider {
    /*
     Class used to learn the process to create a token manually
     So, because of this, I'm commenting every line, for reinforce the learning

     This class also is used to learn the process of validating a token
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
    private String buildToken(String username) {
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
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(key.getBytes()); // Generate a hmacShaKey from bytes of our key (that was got from environment variables)
    }

    // Method used to try to take the claims, if it's successful, return true, else return false
    public boolean isTokenValid(String token) {
        try{
            getClaims(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    // Method used to validate and collect the claims/Payload (User data) from the token
    private Claims getClaims(String token) {
        return Jwts.parser()// Call the JWT parser
                .verifyWith(getSigningKey()) // Set our signing key, as verifier of token's key
                .build() // I don't know why of this
                .parseSignedClaims(token) // Validate the token (If it was created with our key, and if it's not expired), and return the claims
                .getPayload(); // Collect the payLoad from the claims
    }

    // Method used to get the username,
    public String getUsername(String token) {
        return getClaims(token).getSubject(); //Collect the subject by the claims
    }
}
