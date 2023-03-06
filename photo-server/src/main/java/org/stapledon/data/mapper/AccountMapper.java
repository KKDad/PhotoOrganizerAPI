package org.stapledon.data.mapper;

import org.springframework.stereotype.Component;
import org.stapledon.data.entities.Account;
import org.stapledon.data.model.AccountAto;

import java.util.EnumSet;

@Component
public class AccountMapper {
    public AccountAto toAto(Account account) {
        return AccountAto.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .roles(account.getRoles())
                .build();
    }

    public Account toModel(AccountAto accountAto) {
        return Account.builder()
                .firstName(accountAto.getFirstName())
                .lastName(accountAto.getLastName())
                .roles(EnumSet.copyOf(accountAto.getRoles()))
                .build();
    }
}
