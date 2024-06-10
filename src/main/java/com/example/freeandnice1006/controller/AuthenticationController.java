package com.example.freeandnice1006.controller;


import com.example.freeandnice1006.payload.request.AuthenticationRequest;
import com.example.freeandnice1006.payload.request.RefreshTokenRequest;
import com.example.freeandnice1006.payload.request.RegisterRequest;
import com.example.freeandnice1006.payload.response.AuthenticationResponse;
import com.example.freeandnice1006.payload.response.RefreshTokenResponse;
import com.example.freeandnice1006.repository.UserRepository;
import com.example.freeandnice1006.service.api.AuthenticationService;
import com.example.freeandnice1006.service.api.JwtService;
import com.example.freeandnice1006.service.api.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


@Tag(name = "Authentication", description = "The Authentication API. Contains operations like login, logout, refresh-token etc.")
@RestController
@RequestMapping("/api")
@SecurityRequirements()
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Operation(summary = "Регистрация пользователя", description = "Этот эндпоинт позволяет пользователю зарегистрироваться.")
    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestPart RegisterRequest request) throws  ParseException {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success"); //
        response.put("notify", "Вы успешно зарегистрировались");
        response.put("answer", "registration success"); //
        AuthenticationResponse authenticationResponse = authenticationService.register(request);
        userRepository.findByEmail(request.getEmail()).orElse(null);
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(authenticationResponse.getAccessToken());
        ResponseCookie refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(authenticationResponse.getRefreshToken());
        return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body(response);
    }
    @Operation(summary = "Вход пользователя", description = "Этот эндпоинт позволяет пользователю войти в систему.")
    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        response.put("status", "success");
        response.put("notify", "Успешный вход в систему");
        response.put("answer", "login success");
        errors.put("email", "");
        errors.put("password", "");
        //userRepository.findByEmail(request.getEmail());
        //User user = userOptional.get();
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(authenticationResponse.getAccessToken());
        ResponseCookie refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(authenticationResponse.getRefreshToken());
        response.put("accessToken",jwtCookie.getValue().toString());
        response.put("refreshToken", refreshTokenCookie.getValue().toString());
        response.put("tokenType","Bearer");
        return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE,jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE,refreshTokenCookie.toString())
                    .body(response);
    }

    @Operation(summary = "Активация пользователя", description = "Этот эндпоинт позволяет активировать пользователя.")
    @PostMapping("/activate")
    public ResponseEntity<Object> activateUser(@RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        response.put("status", "success");
        response.put("notify", "Активация успешна");
        response.put("answer", "activate success");
        errors.put("code", "");

        String code = requestBody.get("code");

        if (userRepository.findByActivationCode(code) == null) {
            errors.put("code", "Неверный код");
        }

        int count = 0;
        for (Map.Entry<String, String> entry : errors.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (!key.isEmpty() && value.isEmpty()) {
                count++;
            }
        }

        if (count == 1) {
            authenticationService.activateUser(code);
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.put("status", "error");
        response.put("notify", "Некорректные данные");
        response.put("answer", "login error");
        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "Обновление токена", description = "Этот эндпоинт позволяет обновить токен.")
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenService.generateNewToken(request));
    }
    @Operation(summary = "Обновление токена через куки", description = "Этот эндпоинт позволяет обновить токен с использованием куки.")
    @PostMapping("/refresh-token-cookie")
    public ResponseEntity<Void> refreshTokenCookie(HttpServletRequest request) {
        String refreshToken = refreshTokenService.getRefreshTokenFromCookies(request);
        RefreshTokenResponse refreshTokenResponse = refreshTokenService
                .generateNewToken(new RefreshTokenRequest(refreshToken));
        ResponseCookie NewJwtCookie = jwtService.generateJwtCookie(refreshTokenResponse.getAccessToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, NewJwtCookie.toString())
                .build();
    }
    @Operation(summary = "Выход из системы", description = "Этот эндпоинт позволяет пользователю выйти из системы.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        String refreshToken = refreshTokenService.getRefreshTokenFromCookies(request);
        if(refreshToken != null) {
           refreshTokenService.deleteByToken(refreshToken);
        }
        ResponseCookie jwtCookie = jwtService.getCleanJwtCookie();
        ResponseCookie refreshTokenCookie = refreshTokenService.getCleanRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE,refreshTokenCookie.toString())
                .build();

    }
}
