package com.decagon.oxygen.controllers;

import com.decagon.oxygen.entities.User;
import com.decagon.oxygen.pojos.APIResponse;
import com.decagon.oxygen.pojos.AuthRequest;
import com.decagon.oxygen.pojos.GoogleOauth2User;
import com.decagon.oxygen.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/api/auth")
    public ResponseEntity<APIResponse> authenticate(@RequestBody AuthRequest request){
        System.out.println(request.getUsername());
        return  userService.authenticate(request);
    }
    @PostMapping("/api/oauth2/callback")
    public ResponseEntity<APIResponse> authenticateOauth2(@RequestBody GoogleOauth2User principal){
        return  userService.authenticateOauth2(principal);
    }
}
