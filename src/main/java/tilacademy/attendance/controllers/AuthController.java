package tilacademy.attendance.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import tilacademy.attendance.dto.auth.LoginRequest;
import tilacademy.attendance.dto.auth.LoginResponse;
import tilacademy.attendance.dto.auth.TokenRefreshRequest;
import tilacademy.attendance.dto.auth.TokenRefreshResponse;
import tilacademy.attendance.dto.auth.MeResponse;
import tilacademy.attendance.entities.RefreshToken;
import tilacademy.attendance.entities.User;
import tilacademy.attendance.repositories.UserRepository;
import tilacademy.attendance.security.JwtService;
import tilacademy.attendance.services.RefreshTokenService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authManager,
                          UserDetailsService userDetailsService,
                          JwtService jwtService,
                          RefreshTokenService refreshTokenService,
                          UserRepository userRepository) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        UserDetails ud = userDetailsService.loadUserByUsername(req.username());
        String accessToken = jwtService.generateAccessToken(ud.getUsername());

        User user = userRepository.findByUsername(ud.getUsername()).orElseThrow();
        RefreshToken rt = refreshTokenService.createRefreshToken(user);
        String refreshToken = rt.getToken();

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestToken = request.refreshToken();

        var opt = refreshTokenService.findByToken(requestToken);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid refresh token"));
        }

        RefreshToken existing = opt.get();

        try {
            refreshTokenService.verifyExpiration(existing);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }

        User user = existing.getUser();
        String newAccessToken = jwtService.generateAccessToken(user.getUsername());
        RefreshToken newRefresh = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, newRefresh.getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username is required"));
        }
        userRepository.findByUsername(username).ifPresent(user -> refreshTokenService.deleteByUser(user));
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthenticated"));
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        MeResponse resp = new MeResponse(user.getId(), user.getUsername(), user.getRole().name(), user.isEnabled());
        return ResponseEntity.ok(resp);
    }
}
