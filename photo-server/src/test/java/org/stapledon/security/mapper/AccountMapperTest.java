package org.stapledon.security.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.entities.enums.UserRole;
import org.stapledon.security.filter.UserInfoDetails;
import org.stapledon.security.model.RoleAto;
import org.stapledon.security.model.UserInfoResponse;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class AccountMapperTest {

    @InjectMocks
    private AccountMapper accountMapper;

    @Test
    void testToModel() {
        UserInfo userInfo = generateTestUserInfo();
        UserInfoResponse response = accountMapper.toModel(userInfo);

        // Assertions to verify the response
        assertThat(response.getEmail()).isEqualTo(userInfo.getEmail());
        assertThat(response.getPassword()).isEqualTo(userInfo.getPassword());
        assertThat(response.getUsername()).isEqualTo(userInfo.getUsername());
        assertThat(response.getFirstName()).isEqualTo(userInfo.getFirstName());
        assertThat(response.getLastName()).isEqualTo(userInfo.getLastName());
        assertThat(response.getRoles())
                .hasSize(2)
                .containsExactlyInAnyOrder(RoleAto
                                .builder()
                                .roleName(UserRole.ROLE_ADMIN)
                                .build(),
                        RoleAto
                                .builder()
                                .roleName(UserRole.ROLE_USER)
                                .build());
    }

    @Test
    void testToUserInfo() {
        UserInfoResponse response = UserInfoResponse.builder()
                .email("test@stapledon.ca")
                .password("password")
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .roles(Set.of(RoleAto
                        .builder()
                        .roleName(UserRole.ROLE_ADMIN)
                        .build()))
                .build();

        UserInfo userInfo = accountMapper.toUserInfo(response);

        // Assertions to verify userInfo
        assertThat(userInfo.getEmail()).isEqualTo(response.getEmail());
        assertThat(userInfo.getPassword()).isEqualTo(response.getPassword());
        assertThat(userInfo.getUsername()).isEqualTo(response.getUsername());
        assertThat(userInfo.getFirstName()).isEqualTo(response.getFirstName());
        assertThat(userInfo.getLastName()).isEqualTo(response.getLastName());
        assertThat(response.getRoles())
                .hasSize(1)
                .containsExactlyInAnyOrder(RoleAto
                        .builder()
                        .roleName(UserRole.ROLE_ADMIN)
                        .build());
    }

    @Test
    void testToUserInfoDetails() {
        UserInfo userInfo = generateTestUserInfo();

        UserInfoDetails details = accountMapper.toUserInfoDetails(userInfo);

        // Assertions to verify details
        assertThat(details.getUsername()).isEqualTo(userInfo.getUsername());
        assertThat(details.getPassword()).isEqualTo(userInfo.getPassword());
        assertThat(details.getAuthorities())
                .hasSize(2);
    };

    private UserInfo generateTestUserInfo() {
        return UserInfo.builder()
                .email("test@stapledon.ca")
                .password("password")
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .roles(Set.of(org.stapledon.security.entities.Role
                                .builder()
                                .roleName(UserRole.ROLE_ADMIN)
                                .build(),
                        org.stapledon.security.entities.Role
                                .builder()
                                .roleName(UserRole.ROLE_USER)
                                .build()))
                .build();
    }
}