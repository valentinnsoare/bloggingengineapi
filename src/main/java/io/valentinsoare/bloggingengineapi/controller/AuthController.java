package io.valentinsoare.bloggingengineapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.valentinsoare.bloggingengineapi.dto.JWTAuthResponseDto;
import io.valentinsoare.bloggingengineapi.dto.LoginDto;
import io.valentinsoare.bloggingengineapi.dto.RegisterDto;
import io.valentinsoare.bloggingengineapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Login to the application."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful."
    )
    public ResponseEntity<JWTAuthResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        String token = authService.login(loginDto);

        JWTAuthResponseDto authResponse = JWTAuthResponseDto.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/signup")
    @Operation(
            summary = "Register",
            description = "Register to the application."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Registration successful."
    )
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDto registerDto) {
       return new ResponseEntity<>(authService.register(registerDto), HttpStatus.CREATED);
    }
}
