package org.stapledon.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.stapledon.security.entities.Role;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.entities.enums.UserRole;
import org.stapledon.security.filter.UserInfoDetails;
import org.stapledon.security.mapper.AccountMapper;
import org.stapledon.security.repository.UserInfoRepository;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @Mock
    private UserInfoRepository repository;
    @Spy
    private AccountMapper mapper;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    UserInfoService userInfoService;

   @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userInfoService, "encoder", encoder);
    }

    @Test
    void loadUserByUsernameSuccess() {
        when(repository.findByUsername(anyString())).thenReturn(
                Optional.of(UserInfo.builder()
                        .userId(5)
                        .username("username")
                        .password("password")
                        .firstName("firstName")
                        .lastName("lastName")
                        .email("email@stapledon.ca")
                        .roles(Set.of(Role
                                .builder().roleName(UserRole.ROLE_ADMIN).build()))
                        .build()));

        UserInfoDetails result = userInfoService.loadUserByUsername("username");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("username");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getAuthorities()).isNotEmpty();
        assertThat(result.getAuthorities().size()).isEqualTo(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
        assertThat(result.isEnabled()).isTrue();
        assertThat(result.isAccountNonExpired()).isTrue();
        assertThat(result.isAccountNonLocked()).isTrue();
        assertThat(result.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void addUser() {
        when(repository.save(any(UserInfo.class))).thenReturn(UserInfo.builder().build());
        when(encoder.encode(any())).thenReturn("password");
        UserInfo result = userInfoService.addUser(UserInfo.builder().build());
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isNull();
    }
}