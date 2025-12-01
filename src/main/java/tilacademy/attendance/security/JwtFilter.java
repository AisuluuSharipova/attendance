package tilacademy.attendance.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService,
                     CustomUserDetailsService uds) {
        this.jwtService = jwtService;
        this.userDetailsService = uds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        System.out.println("JwtFilter -> Authorization header raw: " + auth);

        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            System.out.println("JwtFilter -> token (first 40 chars): " + (token.length()>40 ? token.substring(0,40)+"..." : token));

            try {
                boolean valid = jwtService.validateToken(token);
                System.out.println("JwtFilter -> token valid: " + valid);
                String username = jwtService.extractUsername(token);
                System.out.println("JwtFilter -> extracted username: " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && valid) {
                    UserDetails details = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("JwtFilter -> authentication set for: " + username);
                }
            } catch (Exception ex) {
                System.out.println("JwtFilter -> token parse/validate exception: " + ex.getClass().getSimpleName() + " / " + ex.getMessage());
            }
        } else {
            System.out.println("JwtFilter -> no Authorization header or not Bearer");
        }

        filterChain.doFilter(request, response);
    }

}
