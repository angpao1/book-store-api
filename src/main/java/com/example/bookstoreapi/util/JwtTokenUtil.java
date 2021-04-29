package com.example.bookstoreapi.util;

import com.example.bookstoreapi.models.User;
import io.jsonwebtoken.*;
import java.util.Date;

public class JwtTokenUtil {

    private static String secret = "12345678900987654321123456789009876543211234567890";

    public static String createJWT(User user) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuer("book-store-api");
        jwtBuilder.setSubject(user.getUsername());
        jwtBuilder.claim("name", user.getName());
        jwtBuilder.claim("surname", user.getSurname());
        jwtBuilder.claim("user_id", user.getUser_id());
        jwtBuilder.claim("date_of_birth", user.getDate_of_birth());
        jwtBuilder.setIssuedAt(now);
        jwtBuilder.signWith(SignatureAlgorithm.HS256, secret);
        String jws = jwtBuilder.compact();

        return jws;
    }

    public static Jws<Claims> verifyToken(String jwt) {
        Jws<Claims> jws;

        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(jwt);
            return jws;
        }
        catch (JwtException ex) {
            return null;
        }
    }
}
