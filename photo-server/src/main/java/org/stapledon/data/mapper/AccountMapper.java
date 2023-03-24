package org.stapledon.data.mapper;

import org.springframework.stereotype.Component;
import org.stapledon.data.entities.Account;
import org.stapledon.data.entities.Role;
import org.stapledon.data.entities.enums.AccountRole;
import org.stapledon.data.model.AccountAto;
import org.stapledon.data.model.RoleAto;
import org.yaml.snakeyaml.util.EnumUtils;

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
                .username(accountAto.getUsername())
                .firstName(accountAto.getFirstName())
                .lastName(accountAto.getLastName())
                .roles(roles)
                .build();
    }
}
