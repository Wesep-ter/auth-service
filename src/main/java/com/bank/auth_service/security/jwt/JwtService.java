package com.bank.auth_service.security.jwt;
import com.bank.auth_service.dto.JwtAuthenticationDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtService {

    @Value("61c4988c015b91c7998d0d7357d7e182851a1384f035c14825bd5e2be421a58c")
    private String key;

    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);

    private final long jwtExpiration = 3600000;

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    public JwtAuthenticationDto RefreshBaseToken(String username, String refreshToken){
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public JwtAuthenticationDto generateAuthToken(String username){
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(generateRefreshToken(username));
        return jwtDto;
    }

    public String getUsernameFromToken(String token){
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

    public String generateJwtToken(String username){
        Date now = new Date();
        Date expireDate = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expireDate)
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