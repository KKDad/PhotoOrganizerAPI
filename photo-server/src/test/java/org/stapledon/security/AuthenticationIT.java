package org.stapledon.security;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.stapledon.AbstractIntegrationTest;
import org.stapledon.StapledonUserGivens;
import org.stapledon.security.dto.AuthRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class AuthenticationIT extends AbstractIntegrationTest {


    @Test
    void generateTokenTest() throws Exception {
        var context = given().givenUser(StapledonUserGivens.UserInfoParameters.builder().build());

        var request = AuthRequest.builder()
                .username(context.username())
                .password(context.password())
                .build();

        String jsonRequest = objectMapper().writeValueAsString(request);

        mockMvc().perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotNull());
    }
}
