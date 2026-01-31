package com.example.hospital.config;

import com.example.hospital.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtUtil jwtUtil;

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain) throws ServletException, IOException {

                final String authHeader = request.getHeader("Authorization");

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        filterChain.doFilter(request, response);
                        return;
                }

                try {
                        final String jwt = authHeader.substring(7);
                        final String userEmail = jwtUtil.extractUsername(jwt);

                        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                                if (jwtUtil.validateToken(jwt)) {
                                        String role = jwtUtil.extractRole(jwt);

                                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                                        userEmail,
                                                        null,
                                                        List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                                        authToken.setDetails(
                                                        new WebAuthenticationDetailsSource().buildDetails(request));
                                        SecurityContextHolder.getContext().setAuthentication(authToken);
                                }
                        }
                } catch (Exception e) {
                        // Log error but don't fail the request, just don't authenticate
                        System.err.println("JWT Authentication warning: " + e.getMessage());
                }

                filterChain.doFilter(request, response);
        }

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) {
                String path = request.getRequestURI();
                return path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui");
        }
}