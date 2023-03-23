package org.stapledon.data.mapper;

import org.springframework.stereotype.Component;
import org.stapledon.data.entities.Account;
import org.stapledon.data.model.AccountAto;

import java.util.EnumSet;

@Component
public class AccountMapper {
    public AccountAto toAto(Account account) {
        return AccountAto.builder()
                .email(account.getEmail())
                .username(account.getUsername())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .roles(account.getRoles())
                .build();
    }

    public Account toModel(AccountAto accountAto) {
        return Account.builder()
                .email(accountAto.getEmail())
                .username(accountAto.getUsername())
                .firstName(accountAto.getFirstName())
                .lastName(accountAto.getLastName())
                .roles(EnumSet.copyOf(accountAto.getRoles()))
                .build();
    }
}
