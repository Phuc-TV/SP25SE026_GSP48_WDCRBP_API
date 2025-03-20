package SP25SE026_GSP48_WDCRBP_api.security;

import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-access-expiration-milliseconds}")
    private long jwtAccessExpiration;

    @Value("${app.jwt-refresh-expiration-milliseconds}")
    private long jwtRefreshExpiration;

    @Autowired
    UserRepository userRepository;

    public String generateAccessToken(User user) {
        return generateToken(user, jwtAccessExpiration);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, jwtRefreshExpiration);
    }


    public String generateToken(User user, long expiration) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + expiration);

        // Convert User object to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson;
        try {
            userJson = objectMapper.writeValueAsString(user);
        } catch (Exception e) {
            throw new RuntimeException("Error converting user to JSON", e);
        }

        // Encode JSON string (Base64)
        String encodedUserJson = Base64.getEncoder().encodeToString(userJson.getBytes());

        // Add user data to JWT claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userData", encodedUserJson);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();
    }


    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }

    public User getUserFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Lấy dữ liệu mã hóa từ claims
        String encodedUserJson = (String) claims.get("userData");

        if (encodedUserJson == null) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "User data not found in token");
        }

        try {
            // Giải mã Base64
            byte[] decodedBytes = Base64.getDecoder().decode(encodedUserJson);
            String userJson = new String(decodedBytes);

            // Chuyển JSON string thành đối tượng User
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(userJson, User.class);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding user data from JWT", e);
        }
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }
    }

    public boolean isTokenValid(String token, String checkUsername) {
        final String username = getUsernameFromJwt(token);
        return (username.equals(checkUsername) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new Date());
    }
}
