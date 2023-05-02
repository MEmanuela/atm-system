package org.internship.jpaonlinebanking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.internship.jpaonlinebanking.exceptions.ErrorResponse;
import org.internship.jpaonlinebanking.exceptions.UserAuthenticationException;
import org.internship.jpaonlinebanking.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageSource messageSource;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
                                    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        try {
            final String jwt;
            final String username;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                var isTokenValid = tokenRepository.findByToken(jwt)
                        .map(token -> !token.isExpired() && !token.isRevoked())
                        .orElse(false);
                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    throw new UserAuthenticationException(messageSource.getMessage("exception.invalidToken", null, Locale.ENGLISH));
                }
            }
            filterChain.doFilter(request, response);
        } catch (UserAuthenticationException exception) {
            String error = exception.getMessage();
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, error);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (Exception exception) {
            String error = exception.getMessage();
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, error);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

        }
    }
}
