package io.valentinsoare.bloggingengineapi.service;

import io.valentinsoare.bloggingengineapi.dto.LoginDto;
import io.valentinsoare.bloggingengineapi.dto.RegisterDto;
import io.valentinsoare.bloggingengineapi.entity.Role;
import io.valentinsoare.bloggingengineapi.entity.User;
import io.valentinsoare.bloggingengineapi.exception.BloggingEngineException;
import io.valentinsoare.bloggingengineapi.repository.RoleRepository;
import io.valentinsoare.bloggingengineapi.repository.UserRepository;
import io.valentinsoare.bloggingengineapi.security.JwtTokenProvider;
import io.valentinsoare.bloggingengineapi.utilities.AuxiliaryMethods;
import io.valentinsoare.bloggingengineapi.utilities.EncodedPasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private AuxiliaryMethods auxiliaryMethods;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.auxiliaryMethods = AuxiliaryMethods.getInstance();
    }

    @Override
    @Transactional
    public String login(LoginDto loginDto) {
        Authentication authCredentials = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail().trim(),
                        loginDto.getPassword().trim()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authCredentials);
        return jwtTokenProvider.generateToken(authCredentials);
    }

    @Override
    @Transactional
    public String register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw BloggingEngineException.builder()
                    .resourceName("register user")
                    .message("Username already exists!")
                    .resources(Map.of("username", registerDto.getUsername()))
                    .build();
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw BloggingEngineException.builder()
                    .resourceName("register user")
                    .message("Email already exists!")
                    .resources(Map.of("email", registerDto.getEmail()))
                    .build();
        }

        User newUser = User.builder()
                .name(registerDto.getName())
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .roles(new HashSet<>())
                .password(String.format("{bcrypt}%s", EncodedPasswordGenerator.encodePassword(registerDto.getPassword())))
                .build();

        for (String i : registerDto.getRoles()) {
            Role role = roleRepository.findByName("ROLE_" + i).orElseThrow(() ->
                BloggingEngineException.builder()
                        .resourceName("register user")
                        .message("Role not found!")
                        .resources(Map.of("role", i))
                        .build()
            );

            newUser.addRole(role);
        }

        userRepository.save(newUser);
        return String.format("\"%s\" registered successfully!", registerDto.getUsername());
    }
}
