package org.stapledon.controller;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stapledon.data.entities.Role;
import org.stapledon.data.model.AccountAto;
import org.stapledon.service.AccountService;

import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void simpleJPATest() {
        Long id = RandomUtils.nextLong();
        AccountAto accountEntity = AccountAto.builder()
                .email("Douglas.Adams@theLastRestaurant.org")
                .firstName("Douglas")
                .lastName("Adams")
                .roles(EnumSet.of(Role.ADMIN))
                .build();

        doReturn(accountEntity).when(accountService).fetch(id);
        AccountAto result = accountController.fetchAccount(id);

        assertThat(result.getFirstName()).isEqualTo("Douglas");
        assertThat(result.getLastName()).isEqualTo("Adams");
        assertThat(result.getRoles()).containsExactly(Role.ADMIN);
    }

    @Test
    void allUsers() {
        when(accountService.fetchAll()).thenReturn(List.of(
                AccountAto
                        .builder()
                        .email("notreal@email.com")
                        .firstName("John")
                        .lastName("Doe")
                        .roles(EnumSet.of(Role.USER))
                        .build(),
                AccountAto.builder()
                        .email("notreal2@email.com")
                        .firstName("Jane")
                        .lastName("Doe")
                        .roles(EnumSet.of(Role.ADMIN, Role.USER))
                        .build()));
        var results = accountController.fetchAllAccounts();

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getFirstName()).isEqualTo("John");
        assertThat(results.get(0).getLastName()).isEqualTo("Doe");
        assertThat(results.get(0).getRoles()).containsExactly(Role.USER);

        assertThat(results.get(1).getFirstName()).isEqualTo("Jane");
        assertThat(results.get(1).getLastName()).isEqualTo("Doe");
        assertThat(results.get(1).getRoles()).containsExactly(Role.ADMIN, Role.USER);
    }
}