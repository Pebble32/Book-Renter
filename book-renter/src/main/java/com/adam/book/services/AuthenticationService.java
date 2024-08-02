package com.adam.book.services;

import com.adam.book.controllers.dtos.AuthenticationRequest;
import com.adam.book.controllers.dtos.AuthenticationResponse;
import com.adam.book.controllers.dtos.RegistrationRequest;
import com.adam.book.repositories.entities.TokenEntity;
import com.adam.book.repositories.entities.UserEntity;
import com.adam.book.repositories.RoleRepository;
import com.adam.book.repositories.TokenRepository;
import com.adam.book.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final static int CODE_LENGTH = 6;
    private final AuthenticationManager authenticationManager;

    @Value("${application.security.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER ROLE not initialized"));
        var user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);

    }

    private void sendValidationEmail(UserEntity user) throws MessagingException {

        var newToken = generateAndSaveActivationToken(user);


        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(UserEntity user) {

        String generatedToken = generateActivationCode();

        var token = TokenEntity.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((UserEntity) auth.getPrincipal());
        System.out.println(user.getUsername());
        claims.put("fullName", user.getFullName());

        System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
        var jwtToken = jwtService.generateToken(claims, user);
        System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCC");

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    //@Transactional
    public void activateAccount(String token) throws MessagingException {
        TokenEntity savedToken = tokenRepository.findByToken(token)
                // todo better exception handling
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation code has expired. New token has been sent to your email address");
        }
        var user = userRepository.findById(savedToken.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
