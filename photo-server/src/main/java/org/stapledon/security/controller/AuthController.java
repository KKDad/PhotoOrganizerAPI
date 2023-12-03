package org.stapledon.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stapledon.security.dto.AuthRequest;
import org.stapledon.security.service.AuthenticationService;
import org.stapledon.security.service.UserInfoService;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserInfoService userInfoFacade;
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        return authenticationService.authenticate(authRequest);
    }
}
