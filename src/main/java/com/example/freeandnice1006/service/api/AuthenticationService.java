package com.example.freeandnice1006.service.api;



import com.example.freeandnice1006.payload.request.AuthenticationRequest;
import com.example.freeandnice1006.payload.request.RegisterRequest;
import com.example.freeandnice1006.payload.response.AuthenticationResponse;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request) throws ParseException;
    AuthenticationResponse authenticate(AuthenticationRequest request);

    boolean activateUser(String code);
}
