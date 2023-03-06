package org.internship.jpaonlinebanking.security;

import lombok.RequiredArgsConstructor;
import org.internship.jpaonlinebanking.config.JwtService;
import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.Token;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.repositories.TokenRepository;
import org.internship.jpaonlinebanking.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .phone("0000000000")
                .email(request.getName() + "@gmail.com")
                .personalCodeNumber("0000000000000")
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(new Role(Long.valueOf(1), "admin"))
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);
    }
    private void revokeAllUserTokens(User user) {
        var validUserToken =
                tokenRepository.findAllByIsExpiredOrIsRevokedAndUser_UserId(
                        false, false, user.getUserId());
        if (validUserToken.isEmpty()) return;
        validUserToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

}
