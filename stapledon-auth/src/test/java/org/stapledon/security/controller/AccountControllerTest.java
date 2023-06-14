package org.stapledon.security.controller;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stapledon.security.domain.enums.AccountRole;
import org.stapledon.security.model.AccountAto;
import org.stapledon.security.model.RoleAto;
import org.stapledon.security.controller.AccountController;
import org.stapledon.security.service.AccountService;

import java.util.List;
import java.util.Set;

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
                .username("Douglas.Adams")
                .firstName("Douglas")
                .lastName("Adams")
                .password("password")
                .roles(Set.of(RoleAto.builder().roleName(AccountRole.ROLE_ADMIN).build()))
                .build();

        doReturn(accountEntity).when(accountService).fetch(id);
        AccountAto result = accountController.fetchAccount(id);

        assertThat(result.getFirstName()).isEqualTo("Douglas");
        assertThat(result.getLastName()).isEqualTo("Adams");
        assertThat(result.getRoles()).containsExactly(RoleAto.builder().roleName(AccountRole.ROLE_ADMIN).build());
    }

    @Test
    void allUsers() {
        when(accountService.fetchAll()).thenReturn(List.of(
                AccountAto
                        .builder()
                        .email("notreal@email.com")
                        .username("notreal")
                        .firstName("John")
                        .lastName("Doe")
                        .password("password")
                        .roles(Set.of(RoleAto.builder().roleName(AccountRole.ROLE_USER).build()))
                        .build(),
                AccountAto.builder()
                        .email("notreal2@email.com")
                        .username("notreal2")
                        .firstName("Jane")
                        .lastName("Doe")
                        .password("password")
                        .roles(Set.of(RoleAto.builder().roleName(AccountRole.ROLE_ADMIN).build(),
                                RoleAto.builder().roleName(AccountRole.ROLE_USER).build()))
                        .build()));
        var results = accountController.fetchAllAccounts();

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getEmail()).isEqualTo("notreal@email.com");
        assertThat(results.get(0).getUsername()).isEqualTo("notreal");
        assertThat(results.get(0).getFirstName()).isEqualTo("John");
        assertThat(results.get(0).getLastName()).isEqualTo("Doe");
        assertThat(results.get(0).getRoles()).containsExactly(RoleAto.builder().roleName(AccountRole.ROLE_USER).build());

        assertThat(results.get(1).getEmail()).isEqualTo("notreal2@email.com");
        assertThat(results.get(1).getUsername()).isEqualTo("notreal2");
        assertThat(results.get(1).getFirstName()).isEqualTo("Jane");
        assertThat(results.get(1).getLastName()).isEqualTo("Doe");
        assertThat(results.get(1).getRoles()).containsExactlyInAnyOrder(RoleAto.builder().roleName(AccountRole.ROLE_ADMIN).build(),
                RoleAto.builder().roleName(AccountRole.ROLE_USER).build());
    }
}