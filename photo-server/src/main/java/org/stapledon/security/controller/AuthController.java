package org.stapledon.security.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stapledon.security.dto.AuthRequest;
import org.stapledon.security.service.AuthenticationService;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;


    /**
     * Authenticates the user and returns an authentication token.
     *
     * @param authRequest the authentication request object containing the user credentials
     * @return the authentication token
     */
    @ApiOperation("Authenticates the user and returns an authentication token")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Successfully authenticated and obtained token"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        return authenticationService.authenticate(authRequest);
    }
}
