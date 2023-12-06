package dev.magadiflo.springsecurityjwtcookie.controllers;

import dev.magadiflo.springsecurityjwtcookie.dtos.AuthRequestDTO;
import dev.magadiflo.springsecurityjwtcookie.dtos.JwtResponseDTO;
import dev.magadiflo.springsecurityjwtcookie.utilities.JwtUtility;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final JwtUtility jwtUtility;
    private final AuthenticationManager authenticationManager;
    @Value("${jwt.cookieExpiry}")
    private Integer cookieExpiry;

    @PostMapping(path = "/login")
    public JwtResponseDTO authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response) {
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            String accessToken = this.jwtUtility.GenerateToken(authRequestDTO.getUsername());

            // Set accessToken to cookie header
            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(this.cookieExpiry)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .token("Aquí iría el refreshToken")
                    .build();
        }
        throw new UsernameNotFoundException("Solicitud de usuario inválido");
    }
}
