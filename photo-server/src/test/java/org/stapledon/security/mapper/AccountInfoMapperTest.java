package org.stapledon.security.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.stapledon.security.dto.AccountInfoDto;
import org.stapledon.security.entities.AccountInfo;
import org.stapledon.security.entities.Role;
import org.stapledon.security.entities.enums.AccountRole;
import org.stapledon.security.filter.AccountInfoDetails;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AccountInfoMapperTest {

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AccountInfoMapper accountInfoMapper;

    @Test
    void testToDto() {
        AccountInfo accountInfo = generateTestUserInfo();
        AccountInfoDto response = accountInfoMapper.toDto(accountInfo);

        // Assertions to verify the response
        assertThat(response.getEmail()).isEqualTo(accountInfo.getEmail());
        assertThat(response.getPassword()).isNull();
        assertThat(response.getUsername()).isEqualTo(accountInfo.getUsername());
        assertThat(response.getFirstName()).isEqualTo(accountInfo.getFirstName());
        assertThat(response.getLastName()).isEqualTo(accountInfo.getLastName());
        assertThat(response.getRoles())
                .hasSize(2)
                .containsExactlyInAnyOrder(AccountRole.ROLE_ADMIN.toString(), AccountRole.ROLE_USER.toString());
    }

    @Test
    void testToUserInfo() {
        when(encoder.encode(anyString())).thenReturn("encodedPassword");

        AccountInfoDto response = AccountInfoDto.builder()
                .email("test@stapledon.ca")
                .password("password")
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .roles(Set.of(AccountRole.ROLE_ADMIN.toString()))
                .build();

        AccountInfo accountInfo = accountInfoMapper.toAccountInfo(response);

        // Assertions to verify accountInfo
        assertThat(accountInfo.getEmail()).isEqualTo(response.getEmail());
        assertThat(accountInfo.getPassword()).isEqualTo("encodedPassword");
        assertThat(accountInfo.getUsername()).isEqualTo(response.getUsername());
        assertThat(accountInfo.getFirstName()).isEqualTo(response.getFirstName());
        assertThat(accountInfo.getLastName()).isEqualTo(response.getLastName());
        assertThat(response.getRoles())
                .hasSize(1)
                .containsExactlyInAnyOrder(AccountRole.ROLE_ADMIN.toString());
    }

    @Test
    void testToUserInfoDetails() {
        AccountInfo accountInfo = generateTestUserInfo();

        AccountInfoDetails details = accountInfoMapper.toSecurityAccountInfoDetails(accountInfo);

        // Assertions to verify details
        assertThat(details.getUsername()).isEqualTo(accountInfo.getUsername());
        assertThat(details.getPassword()).isEqualTo(accountInfo.getPassword());
        assertThat(details.getAuthorities())
                .hasSize(2);
    };

    @Test
    void testMerge() {
        when(encoder.encode(anyString())).thenReturn("encodedPassword");

        AccountInfo destination = generateTestUserInfo();
        AccountInfoDto source = AccountInfoDto.builder()
                .email("test@stapledon.ca")
                .password("newPassword")
                .username("newUsername")
                .firstName("newFirstName")
                .lastName("newLastName")
                .build();

        accountInfoMapper.merge(destination, source);

        // Assertions to verify destination
        assertThat(destination.getEmail()).isEqualTo(source.getEmail());
        assertThat(destination.getPassword()).isEqualTo("encodedPassword");
        assertThat(destination.getUsername()).isEqualTo(source.getUsername());
        assertThat(destination.getFirstName()).isEqualTo(source.getFirstName());
        assertThat(destination.getLastName()).isEqualTo(source.getLastName());
    }

    private AccountInfo generateTestUserInfo() {
        return AccountInfo.builder()
                .email("test@stapledon.ca")
                .password("password")
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .roles(Set.of(
                        Role.builder()
                            .roleName(AccountRole.ROLE_ADMIN)
                            .build(),
                        Role.builder()
                            .roleName(AccountRole.ROLE_USER)
                            .build()))
                .build();
    }
}