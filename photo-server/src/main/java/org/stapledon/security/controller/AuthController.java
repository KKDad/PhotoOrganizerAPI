package org.stapledon.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.model.AuthRequest;
import org.stapledon.security.service.JwtService;
import org.stapledon.security.service.UserInfoService;

import java.net.URI;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserInfoService userInfoFacade;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/addNewUser")
    public ResponseEntity<Object> addNewUser(@RequestBody UserInfo userInfo) {
        UserInfo createdUser = userInfoFacade.addUser(userInfo);
        return ResponseEntity.created(URI.create("/api/v1/users/" + createdUser.getUserId())).build();
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
