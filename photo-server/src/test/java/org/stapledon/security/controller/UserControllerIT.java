package org.stapledon.security.controller;

import org.stapledon.AbstractIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.stapledon.security.dto.UserInfoDto;
import org.stapledon.security.entities.enums.UserRole;


import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;

class UserControllerIT extends AbstractIntegrationTest {

    @Test
    void createUserOkTest() throws Exception {
        String token = given().givenAdministrator().authenticate();


        var request = UserInfoDto.builder()
                .username("user1")
                .password("F@kePassw0rd")
                .email("test@stapledon.ca")
                .firstName("Test")
                .lastName("User")
                .roles(Set.of(UserRole.ROLE_USER.toString()))
                .build();

        String jsonRequest = objectMapper().writeValueAsString(request);

        mockMvc().perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .header("Authorization", "Bearer " + token)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotNull());
    }

    @Test
    void createUserInsufficientPermissionsTest() throws Exception {
        String token = given().givenUser().authenticate();

        var request = UserInfoDto.builder()
                .username("user1")
                .password("F@kePassw0rd")
                .email("test@stapledon.ca")
                .firstName("Test")
                .lastName("User")
                .roles(Set.of(UserRole.ROLE_USER.toString()))
                .build();

        String jsonRequest = objectMapper().writeValueAsString(request);

        mockMvc().perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .header("Authorization", "Bearer " + token)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}