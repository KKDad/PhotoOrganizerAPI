package org.stapledon.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stapledon.security.domain.Account;
import org.stapledon.security.domain.Role;
import org.stapledon.security.domain.enums.AccountRole;
import org.stapledon.security.mapper.AccountMapper;
import org.stapledon.security.repository.AccountRepository;
import org.stapledon.security.service.AccountService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private AccountMapper accountMapper = new AccountMapper();

    @Test
    void fetchAll() {
        when(accountRepository.findAll()).thenReturn(List.of(getAccount("foo@bar.com")));
        assertEquals(1, accountService.fetchAll().size());
        verify(accountMapper, times(1)).toAto(any());
    }
    @Test
    void fetchAllNoRecords() {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), accountService.fetchAll());
        verify(accountMapper, times(0)).toAto(any());
    }


    @Test
    void fetch() {
        Account account = getAccount("foo@bar.com");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        assertEquals(account.getEmail(), accountService.fetch(1L).getEmail());
        verify(accountMapper, times(1)).toAto(any());
    }

    @Test
    void save() {
        Account account = getAccount("foo@bar.com");
        when(accountRepository.save(any())).thenReturn(account);
        accountService.save(accountMapper.toAto(account));
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void fetchByUsername() {
        Account account = getAccount("foo@bar.com");
        when(accountRepository.findByUsername("foobar")).thenReturn(Optional.of(account));
        assertEquals(account.getEmail(), accountService.fetchByUsername("foobar").getEmail());
        verify(accountMapper, times(1)).toAto(any());
    }

    @Test
    void delete() {
        accountService.delete(1L);
        verify(accountRepository, times(1)).deleteById(1L);
    }

    private static Account getAccount(String email) {
        Account account = Account
                .builder()
                .email(email)
                .username("foobar")
                .firstName("Foo")
                .lastName("Bar")
                .password("password")
                .roles(Set.of(Role.builder().roleName(AccountRole.ROLE_USER).build()))
                .build();
        return account;
    }
}