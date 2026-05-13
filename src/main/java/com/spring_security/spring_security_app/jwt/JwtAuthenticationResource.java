package com.spring_security.spring_security_app.jwt;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@RestController
@Tag(
        name = "User Authentication",
        description = "User Login API: /authenticate"
)
public class JwtAuthenticationResource {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserDetailsService userDetailsService;

    // Inject all three dependencies via constructor
    JwtAuthenticationResource(JwtEncoder jwtEncoder,
                              JwtDecoder jwtDecoder,
                              UserDetailsService userDetailsService) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userDetailsService = userDetailsService;
    }
//    private JwtEncoder jwtEncoder;
//
//    JwtAuthenticationResource(JwtEncoder jwtEncoder){
//        this.jwtEncoder = jwtEncoder;
//    }

//    @PostMapping("/authenticate")
//    public JwtResponse authenticate(Authentication authentication){
//
//        return new JwtResponse(createToken(authentication));
//    }
    @PostMapping("/authenticate")
    public AuthResponse authenticate(Authentication authentication) {
        String accessToken = createToken(authentication, 30);   // 30 min
        String refreshToken = createToken(authentication, 60 * 24 * 7); // 7 days
        return new AuthResponse(accessToken, refreshToken);
    }
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {
        // Decode the refresh token — Spring's decoder validates expiry automatically
        Jwt decoded = jwtDecoder.decode(request.refreshToken());
        String username = decoded.getSubject();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        String newAccessToken = createToken(auth, 30);
        return new AuthResponse(newAccessToken, request.refreshToken());
    }
    private String createToken(Authentication authentication, long minutesDuration){
        var claims = JwtClaimsSet.builder()
                                .issuer("self")
                                .issuedAt(Instant.now())
                                .expiresAt(Instant.now().plusSeconds(60 * minutesDuration))
                                .subject(authentication.getName())
                                .claim("scope", createScope(authentication))
                                .build();
        JwtEncoderParameters parameters = JwtEncoderParameters.from(claims);

        return jwtEncoder.encode(parameters).getTokenValue();
    }

    private String createScope(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                // authorities already have ROLE_ prefix from UserDetailsService
                // remove it here — the converter above adds it back on decode
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                .collect(Collectors.joining(" "));
    }
//    private String createScope(Authentication authentication){
//        return authentication.getAuthorities().stream()
//                .map(a -> a.getAuthority())
//                .collect(Collectors.joining(" "));
//    }
}
record JwtResponse(String token){}
record AuthResponse(String accessToken, String refreshToken) {}
record RefreshRequest(String refreshToken) {}
