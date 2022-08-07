package mashup.spring.jsmr.adapter.infrastructure.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import mashup.spring.jsmr.domain.exception.JwtException;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtProperty jwtProperty;

    private String createToken(final long payload, final String secretKey, final Long tokenValidTime) {
        return Jwts.builder()
                .setSubject(String.valueOf(payload))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .compact();
    }

    public String createAccessToken(final long payload) {
        return createToken(payload, jwtProperty.getAccessTokenSecretKey(), jwtProperty.getAccessTokenValidTime());
    }

    public Long getAccessTokenPayload(String accessToken) {
        try {
            var claims = Jwts.parser()
                    .setSigningKey(jwtProperty.getAccessTokenSecretKey())
                    .parseClaimsJws(accessToken)
                    .getBody();

            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtException();
        }
    }

    public boolean validateToken(String accessToken) {
        try {
            var claims = Jwts.parser()
                    .setSigningKey(jwtProperty.getAccessTokenSecretKey())
                    .parseClaimsJws(accessToken);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
