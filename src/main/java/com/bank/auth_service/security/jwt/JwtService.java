package com.bank.auth_service.security.jwt;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class JwtService {

    @Value("${token.key}")
    private String key;

    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);

    private final long jwtExpiration = 3600000;


    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    public TokenData parseToken(String token){
        Claims claims = getAllClaims(token);
        return TokenData.builder()
                .id((Long)claims.get("id"))
                .name((String)claims.get("name"))
                .lastName((String)claims.get("lastName"))
                .roles((List<String>) claims.get("roles"))
                .build();
    }

    private Claims getAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


//    public JwtAuthenticationDto RefreshBaseToken(String username, String refreshToken){
//        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
//        jwtDto.setToken(generateJwtToken(username));
//        jwtDto.setRefreshToken(refreshToken);
//        return jwtDto;
//    }


//    public JwtAuthenticationDto generateAuthToken(String username){
//        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
//        jwtDto.setToken(generateJwtToken(username));
//        jwtDto.setRefreshToken(generateRefreshToken(username));
//        return jwtDto;
//    }


    public String getLoginFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }


    public boolean validateJwtToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (ExpiredJwtException expEx){
            LOGGER.error("Expired JwtException", expEx);
        }catch (UnsupportedJwtException expEx){
            LOGGER.error("Unsupported JwtException", expEx);
        }catch (MalformedJwtException expEx){
            LOGGER.error("Malformed JwtException", expEx);
        }catch (SecurityException expEx){
            LOGGER.error("Security Exception", expEx);
        }catch (Exception expEx){
            LOGGER.error("Invalid token", expEx);
        }
        return false;
    }


    public String generateJwtToken(CustomUserDetails customUserDetails){
        Date now = new Date();
        Date expireDate = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("id", customUserDetails.getUser().getId());
        claims.put("name", customUserDetails.getUser().getName());
        claims.put("lastName", customUserDetails.getUser().getLastName());
        claims.put("roles", customUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        return Jwts.builder()
                .subject(customUserDetails.getUsername())
                .issuedAt(now)
                .expiration(expireDate)
                .claims(claims)
                .signWith(getSignInKey())
                .compact();
    }


    public String generateRefreshToken(String username){
        Date now = new Date();
        Date expireDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(getSignInKey())
                .compact();
    }
}