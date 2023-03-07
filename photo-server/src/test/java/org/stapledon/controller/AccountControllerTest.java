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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

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
    }
}