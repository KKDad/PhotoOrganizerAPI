package org.stapledon.security.mapper;

import org.springframework.stereotype.Component;
import org.stapledon.security.domain.Account;
import org.stapledon.security.domain.Role;
import org.stapledon.security.domain.enums.AccountRole;
import org.stapledon.security.model.AccountAto;
import org.stapledon.security.model.RoleAto;

@Component
public class AccountMapper {
    public AccountAto toAto(Account account) {
        var roleAto = account.getRoles().stream()
                .map(role -> RoleAto.builder()
                        .roleName(AccountRole.valueOf(role.getRoleName().toString()))
                        .build())
                .collect(java.util.stream.Collectors.toSet());

        return AccountAto.builder()
                .email(account.getEmail())
                .password(account.getPassword())
                .username(account.getUsername())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .roles(roleAto)
                .build();
    }

    public Account toModel(AccountAto accountAto) {
        var roles = accountAto.getRoles().stream()
                .map(role -> Role.builder().roleName(AccountRole.valueOf(role.getRoleName().toString())).build())
                .collect(java.util.stream.Collectors.toSet());

        return Account.builder()
                .email(accountAto.getEmail())
                .password(accountAto.getPassword())
                .username(accountAto.getUsername())
                .firstName(accountAto.getFirstName())
                .lastName(accountAto.getLastName())
                .roles(roles)
                .build();
    }
}
