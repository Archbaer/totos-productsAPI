package com.totos.productsAPI.filter;

import com.totos.productsAPI.services.PublicKeyService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final PublicKeyService publicKeyService;

    public JwtAuthenticationFilter(PublicKeyService publicKeyService) {
        this.publicKeyService = publicKeyService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extract JWT Token from Authorization Header
        String token = extractJwtToken(request);

        // If token exists, it will validate
        if (token != null) {
            try {
                // Fetch public key
                PublicKey publicKey = publicKeyService.getPublicKey();

                // Validate and parse the token
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(publicKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();



                // Extract roles and create authentication
                List<SimpleGrantedAuthority> authorities = extractAuthorities(claims);

                System.out.println("Authorities: " + authorities);

                // Authentication object
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        authorities
                );

                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private List<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        String role = claims.get("role", String.class);
        System.out.println("Role: "+ role);
        return role != null
                ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+ role))
                : Collections.emptyList();
    }

}
