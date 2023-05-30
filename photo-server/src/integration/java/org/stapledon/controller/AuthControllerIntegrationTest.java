package org.stapledon.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.*;
import org.stapledon.GoogleCredentialConfig;
import org.stapledon.security.login.model.LoginRequest;
import org.stapledon.security.login.model.UserInfoResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableJpaRepositories(basePackages = {
        "org.stapledon"
})
@Import(GoogleCredentialConfig.class)
public class AuthControllerIntegrationTest {

    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSignInEndpoint() {
        // Set up request body with login credentials
        LoginRequest loginRequest = LoginRequest.builder()
                .username("testuser")
                .password("testpassword")
                .build();

        // Set up headers with content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create your request entity with body and headers
        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest, headers);

        // Make a POST request to the sign-in endpoint
        ResponseEntity<UserInfoResponse> responseEntity = restTemplate.exchange(
                createURLWithPort("/api/v1/auth/signin"), HttpMethod.POST, requestEntity, UserInfoResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add more assertions based on the expected response from the endpoint
    }
    // Utility method to create the URL with the specified port
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
