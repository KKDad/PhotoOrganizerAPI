package org.stapledon.security.mapper;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.stapledon.security.dto.UserInfoDto;
import org.stapledon.security.entities.Role;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.entities.enums.UserRole;
import org.stapledon.security.filter.UserInfoDetails;

import java.util.HashSet;

@Component
public class UserInfoMapper {

    @Lazy
    @Autowired
    // Do not use remove @Autowired and use constructor injection - Will cause circular dependency
    private PasswordEncoder encoder;

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public UserInfoDto toDto(UserInfo userInfo) {
        var roleAto = userInfo.getRoles().stream()
                .map(role -> role.getRoleName().toString())
                .collect(java.util.stream.Collectors.toSet());

        return UserInfoDto.builder()
                .email(userInfo.getEmail())
                .username(userInfo.getUsername())
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .roles(roleAto)
                .build();
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public UserInfo toUserInfo(UserInfoDto userInfoResponse) {
        return UserInfo.builder()
                .email(userInfoResponse.getEmail())
                .username(userInfoResponse.getUsername())
                .firstName(userInfoResponse.getFirstName())
                .lastName(userInfoResponse.getLastName())
                .password(encoder.encode(userInfoResponse.getPassword()))
                .build();
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public UserInfoDetails toSecurityUserInfoDetails(UserInfo userInfo) {
        return UserInfoDetails.builder()
                .username(userInfo.getUsername())
                .password(userInfo.getPassword())
                .authorities(userInfo.getRoles()
                        .stream()
                        .map(p -> p.getRoleName().toString())
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
    }

    public void merge(UserInfo destination, UserInfoDto source) {
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
        destination.setRoles(new HashSet<>());
        destination.getRoles().addAll(source.getRoles().stream()
                .map(role -> Role.builder().roleName(UserRole.valueOf(role)).build())
                .collect(java.util.stream.Collectors.toSet()));
    }
}
