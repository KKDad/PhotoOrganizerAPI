package org.stapledon.security.mapper;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.stapledon.security.dto.AccountInfoDto;
import org.stapledon.security.entities.AccountInfo;
import org.stapledon.security.filter.AccountInfoDetails;

@Component
public class AccountInfoMapper {

    @Lazy
    @Autowired
    // Do not use remove @Autowired and use constructor injection - Will cause circular dependency
    private PasswordEncoder encoder;

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public AccountInfoDto toDto(AccountInfo accountInfo) {
        var roleAto = accountInfo.getRoles().stream()
                .map(role -> role.getRoleName().toString())
                .collect(java.util.stream.Collectors.toSet());

        return AccountInfoDto.builder()
                .email(accountInfo.getEmail())
                .username(accountInfo.getUsername())
                .firstName(accountInfo.getFirstName())
                .lastName(accountInfo.getLastName())
                .roles(roleAto)
                .build();
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public AccountInfo toAccountInfo(AccountInfoDto accountInfoResponse) {
        return AccountInfo.builder()
                .email(accountInfoResponse.getEmail())
                .username(accountInfoResponse.getUsername())
                .firstName(accountInfoResponse.getFirstName())
                .lastName(accountInfoResponse.getLastName())
                .password(encoder.encode(accountInfoResponse.getPassword()))
                .build();
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public AccountInfoDetails toSecurityAccountInfoDetails(AccountInfo accountInfo) {
        return AccountInfoDetails.builder()
                .username(accountInfo.getUsername())
                .password(accountInfo.getPassword())
                .authorities(accountInfo.getRoles()
                        .stream()
                        .map(p -> p.getRoleName().toString())
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
    }

    public void merge(AccountInfo destination, AccountInfoDto source) {
        if (destination == null || source == null) {
            return;
        }
        if (!Strings.isEmpty(source.getEmail())) {
            destination.setEmail(source.getEmail());
        }
        if (!Strings.isEmpty(source.getFirstName())) {
            destination.setFirstName(source.getFirstName());
        }
        if (!Strings.isEmpty(source.getLastName())) {
            destination.setLastName(source.getLastName());
        }
        if (!Strings.isEmpty(source.getUsername())) {
            destination.setUsername(source.getUsername());
        }
        if (!Strings.isEmpty(source.getPassword())) {
            destination.setPassword(encoder.encode(source.getPassword()));
        }
    }
}
