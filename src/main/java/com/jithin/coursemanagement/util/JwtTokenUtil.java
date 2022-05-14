package com.jithin.coursemanagement.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class JwtTokenUtil {

    public static final String MYCOMPLE_XKEY_12489 = "MYCOMPLEXkey12489";

    public String getUserNameFromTheToken(String token){
        return Jwts.parser().setSigningKey(MYCOMPLE_XKEY_12489)
                .parseClaimsJws(token)
                .getBody().getSubject();

    }
    public String generateToken(String subject, HashMap<String, Object> map)
    {
        return Jwts.builder()
                .setSubject(subject)
                .setClaims(map)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 123456 * 1000))
                .signWith(SignatureAlgorithm.HS512,MYCOMPLE_XKEY_12489).compact();
    }
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(MYCOMPLE_XKEY_12489).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.out.print("Invalid JWT signature: {}"+ e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.print("Invalid JWT token: {}"+ e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.print("JWT token is expired: {}"+ e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.print("JWT token is unsupported: {}"+ e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.print("JWT claims string is empty: {}"+ e.getMessage());
        }
        return false;
    }
}
