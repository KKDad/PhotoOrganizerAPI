package org.stapledon.security.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.stapledon.security.dto.UserInfoDto;
import org.stapledon.security.entities.Role;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.entities.enums.UserRole;
import org.stapledon.security.filter.UserInfoDetails;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserInfoMapperTest {

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserInfoMapper userInfoMapper;

    @Test
    void testToDto() {
        UserInfo userInfo = generateTestUserInfo();
        UserInfoDto response = userInfoMapper.toDto(userInfo);

        // Assertions to verify the response
        assertThat(response.getEmail()).isEqualTo(userInfo.getEmail());
        assertThat(response.getPassword()).isNull();
        assertThat(response.getUsername()).isEqualTo(userInfo.getUsername());
        assertThat(response.getFirstName()).isEqualTo(userInfo.getFirstName());
        assertThat(response.getLastName()).isEqualTo(userInfo.getLastName());
        assertThat(response.getRoles())
                .hasSize(2)
                .containsExactlyInAnyOrder(UserRole.ROLE_ADMIN.toString(), UserRole.ROLE_USER.toString());
    }

    @Test
    void testToUserInfo() {
        when(encoder.encode(anyString())).thenReturn("encodedPassword");

        UserInfoDto response = UserInfoDto.builder()
                .email("test@stapledon.ca")
                .password("password")
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .roles(Set.of(UserRole.ROLE_ADMIN.toString()))
                .build();

        UserInfo userInfo = userInfoMapper.toUserInfo(response);

        // Assertions to verify userInfo
        assertThat(userInfo.getEmail()).isEqualTo(response.getEmail());
        assertThat(userInfo.getPassword()).isEqualTo("encodedPassword");
        assertThat(userInfo.getUsername()).isEqualTo(response.getUsername());
        assertThat(userInfo.getFirstName()).isEqualTo(response.getFirstName());
        assertThat(userInfo.getLastName()).isEqualTo(response.getLastName());
        assertThat(response.getRoles())
                .hasSize(1)
                .containsExactlyInAnyOrder(UserRole.ROLE_ADMIN.toString());
    }

    @Test
    void testToUserInfoDetails() {
        UserInfo userInfo = generateTestUserInfo();

        UserInfoDetails details = userInfoMapper.toSecurityUserInfoDetails(userInfo);

        // Assertions to verify details
        assertThat(details.getUsername()).isEqualTo(userInfo.getUsername());
        assertThat(details.getPassword()).isEqualTo(userInfo.getPassword());
        assertThat(details.getAuthorities())
                .hasSize(2);
    };

    @Test
    void testMerge() {
        when(encoder.encode(anyString())).thenReturn("encodedPassword");

        UserInfo destination = generateTestUserInfo();
        UserInfoDto source = UserInfoDto.builder()
                .email("test@stapledon.ca")
                .password("newPassword")
                .username("newUsername")
                .firstName("newFirstName")
                .lastName("newLastName")
                .roles(Set.of(UserRole.ROLE_ADMIN.toString()))
                .build();

        userInfoMapper.merge(destination, source);

        // Assertions to verify destination
        assertThat(destination.getEmail()).isEqualTo(source.getEmail());
        assertThat(destination.getPassword()).isEqualTo("encodedPassword");
        assertThat(destination.getUsername()).isEqualTo(source.getUsername());
        assertThat(destination.getFirstName()).isEqualTo(source.getFirstName());
        assertThat(destination.getLastName()).isEqualTo(source.getLastName());
        assertThat(destination.getRoles())
                .hasSize(1)
                .satisfies(roles -> assertThat(roles.stream().findFirst().get().getRoleName()).isEqualTo(UserRole.ROLE_ADMIN));
    }

    private UserInfo generateTestUserInfo() {
        return UserInfo.builder()
                .email("test@stapledon.ca")
                .password("password")
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .roles(Set.of(
                        Role.builder()
                            .roleName(UserRole.ROLE_ADMIN)
                            .build(),
                        Role.builder()
                            .roleName(UserRole.ROLE_USER)
                            .build()))
                .build();
    }
}