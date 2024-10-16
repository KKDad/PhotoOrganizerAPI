package org.stapledon.security.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.stapledon.AbstractIntegrationTest;
import org.stapledon.StapledonAccountGivens;
import org.stapledon.security.dto.AccountInfoDto;
import org.stapledon.security.entities.enums.AccountRole;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class AccountControllerIT extends AbstractIntegrationTest {

    @Test
    void createUserOkTest() throws Exception {
        String token = given().givenAdministrator().authenticate();


        var request = AccountInfoDto.builder()
                .username("user1")
                .password("F@kePassw0rd")
                .email("test@stapledon.ca")
                .firstName("Test")
                .lastName("User")
                .roles(Set.of(AccountRole.ROLE_USER.toString()))
                .build();

        String jsonRequest = objectMapper().writeValueAsString(request);

        mockMvc().perform(MockMvcRequestBuilders.post("/api/v1/accounts")
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

        var request = AccountInfoDto.builder()
                .username("user1")
                .password("F@kePassw0rd")
                .email("test@stapledon.ca")
                .firstName("Test")
                .lastName("User")
                .roles(Set.of(AccountRole.ROLE_USER.toString()))
                .build();

        String jsonRequest = objectMapper().writeValueAsString(request);

        mockMvc().perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                        .header("Authorization", "Bearer " + token)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getAllUsersOkTest() throws Exception {
        String token = given().givenAdministrator().authenticate();

        mockMvc().perform(MockMvcRequestBuilders.get("/api/v1/accounts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotNull());
    }

    @Test
    void getUserOkTest() throws Exception {
        String token = given().givenAdministrator().authenticate();

        var existingUser = given().givenUser(StapledonAccountGivens.AccountInfoParameters.builder()
                .username("user1")
                .build());


        mockMvc().perform(MockMvcRequestBuilders.get("/api/v1/accounts/" + existingUser.id())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotNull());
    }

    @Test
    void updateUserOkTest() throws Exception {
        String token = given().givenAdministrator().authenticate();

        var existingUser = given().givenUser(StapledonAccountGivens.AccountInfoParameters.builder()
                .username("user1")
                .build());

        var request = AccountInfoDto.builder()
                .username("user1")
                .password("F@kePassw0rd")
                .email("test@stapledon.ca")
                .firstName("Test")
                .lastName("User")
                .roles(Set.of(AccountRole.ROLE_USER.toString()))
                .build();

        String jsonRequest = objectMapper().writeValueAsString(request);

        mockMvc().perform(MockMvcRequestBuilders.put("/api/v1/accounts/" + existingUser.id())
                        .header("Authorization", "Bearer " + token)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotNull());
    }

    @Test
    void deleteUserOkTest() throws Exception {
        String token = given().givenAdministrator().authenticate();
        long userId = 1L; 

        mockMvc().perform(MockMvcRequestBuilders.delete("/api/v1/accounts/" + userId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}