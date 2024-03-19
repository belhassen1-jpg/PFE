package com.example.pidev.Controllers;


import com.example.pidev.DTO.JwtRequest;
import com.example.pidev.DTO.JwtResponse;
import com.example.pidev.Services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {


    @Autowired
    JwtService jwtService;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        JwtResponse response = jwtService.createJwtToken(jwtRequest);
        // Get the user ID from the JWT token
        // Create a new JwtResponse object with the user ID and token
        // Return the JwtResponse object as the response body
        return response;    }
}

