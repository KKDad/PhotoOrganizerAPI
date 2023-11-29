package org.stapledon.security.mapper;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.entities.Role;
import org.stapledon.security.filter.UserInfoDetails;
import org.stapledon.security.entities.enums.UserRole;
import org.stapledon.security.model.UserInfoResponse;
import org.stapledon.security.model.RoleAto;

@Component
public class AccountMapper {

    public UserInfoResponse toModel(UserInfo userInfo) {
        var roleAto = userInfo.getRoles().stream()
                .map(role -> RoleAto.builder()
                        .roleName(UserRole.valueOf(role.getRoleName().toString()))
                        .build())
                .collect(java.util.stream.Collectors.toSet());

        return UserInfoResponse.builder()
                .email(userInfo.getEmail())
                .password(userInfo.getPassword())
                .username(userInfo.getUsername())
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .roles(roleAto)
                .build();
    }

    public UserInfo toUserInfo(UserInfoResponse userInfoResponse) {
        var roles = userInfoResponse.getRoles().stream()
                .map(role -> Role.builder().roleName(UserRole.valueOf(role.getRoleName().toString())).build())
                .collect(java.util.stream.Collectors.toSet());

        return UserInfo.builder()
                .email(userInfoResponse.getEmail())
                .password(userInfoResponse.getPassword())
                .username(userInfoResponse.getUsername())
                .firstName(userInfoResponse.getFirstName())
                .lastName(userInfoResponse.getLastName())
                .roles(roles)
                .build();
    }

    public UserInfoDetails toUserInfoDetails(UserInfo userInfo) {
        return UserInfoDetails.builder()
                .username(userInfo.getEmail())
                .password(userInfo.getPassword())
                .authorities(userInfo.getRoles()
                        .stream()
                        .map(p -> p.getRoleName().toString())
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
    }
}
