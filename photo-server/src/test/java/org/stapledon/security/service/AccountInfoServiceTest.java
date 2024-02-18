package org.stapledon.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.stapledon.InvalidParameterException;
import org.stapledon.security.dto.AccountInfoDto;
import org.stapledon.security.entities.AccountInfo;
import org.stapledon.security.entities.Role;
import org.stapledon.security.entities.enums.AccountRole;
import org.stapledon.security.filter.AccountInfoDetails;
import org.stapledon.security.mapper.AccountInfoMapper;
import org.stapledon.security.repository.AccountInfoRepository;
import org.stapledon.security.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountInfoServiceTest {

    @Mock
    private AccountInfoRepository accountInfoRepository;
    @Mock
    private RoleRepository roleRepository;
    @Spy
    private AccountInfoMapper mapper;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    AccountInfoService accountInfoService;

   @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mapper, "encoder", encoder);
        ReflectionTestUtils.setField(accountInfoService, "minPasswordLength", 2);
    }

    @Test
    void loadUserByUsernameSuccess() {
       AccountInfo accountInfo = generateUserInfo(false, true);
        when(accountInfoRepository.findByUsername(anyString())).thenReturn(
                Optional.of(accountInfo));

        AccountInfoDetails result = accountInfoService.loadUserByUsername("username");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(accountInfo.getUsername());
        assertThat(result.getPassword()).isEqualTo(accountInfo.getPassword());
        assertThat(result.getAuthorities()).isNotEmpty().hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
        assertThat(result.isEnabled()).isTrue();
        assertThat(result.isAccountNonExpired()).isTrue();
        assertThat(result.isAccountNonLocked()).isTrue();
        assertThat(result.isCredentialsNonExpired()).isTrue();
    }


    @Test
    void addUser() {
        AccountInfoDto accountInfoDto = generateUserInfoDto(false, true);
        AccountInfo accountInfo = generateUserInfo(false, true);

        when(accountInfoRepository.save(any(AccountInfo.class))).thenReturn(accountInfo);
        when(roleRepository.findByRoleName(any(AccountRole.class))).thenReturn(Optional.of(Role.builder().roleName(AccountRole.ROLE_ADMIN).build()));
        when(accountInfoRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(encoder.encode(any())).thenReturn("password");

        AccountInfoDto result = accountInfoService.addAccount(accountInfoDto);

        verify(accountInfoRepository).findByUsername(anyString());
        verify(roleRepository).findByRoleName(any(AccountRole.class));
        verify(accountInfoRepository).save(any(AccountInfo.class));
        verify(encoder).encode(any());

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(accountInfo.getUsername());
        assertThat(result.getPassword()).isNull();
        assertThat(result.getRoles()).isNotEmpty().hasSize(1);
        assertThat(result.getRoles().iterator().next()).isEqualTo(AccountRole.ROLE_ADMIN.toString());
    }

    @Test
    void addUserInsecurePassword() {
        AccountInfoDto accountInfoDto = generateUserInfoDto(true, false);
        accountInfoDto.setPassword("short");

        assertThatThrownBy(() -> accountInfoService.addAccount(accountInfoDto))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("Password must be at least");
    }

    @Test
    void addUserUsernameExists() {
        AccountInfoDto accountInfoDto = generateUserInfoDto(true, false);
        AccountInfo accountInfo = generateUserInfo(true, false);
        when(accountInfoRepository.findByUsername(anyString())).thenReturn(Optional.of(accountInfo));

        assertThatThrownBy(() -> accountInfoService.addAccount(accountInfoDto))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("User already exists");
    }

    @Test
    void updateUserInsecurePassword() {
        AccountInfoDto accountInfoDto = generateUserInfoDto(true, false);
        accountInfoDto.setPassword("short");

        assertThatThrownBy(() -> accountInfoService.updateAccount(1L, accountInfoDto))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("Password must be at least");
    }

    @Test
    void updateUserNotFound() {
        AccountInfoDto accountInfoDto = generateUserInfoDto(true, false);
        when(accountInfoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountInfoService.updateAccount(1L, accountInfoDto))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("User not found");
    }

    @Test
    void getUserNotFound() {
        when(accountInfoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountInfoService.getAccount(1L))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("User not found");
    }

    @Test
    void updateUserTest() {
        // Arrange
        AccountInfo existingUser = generateUserInfo(true, false);
        AccountInfoDto existingUserDto = generateUserInfoDto(true, false);
        when(accountInfoRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(encoder.encode(any())).thenReturn("password");
        when(roleRepository.findByRoleName(any(AccountRole.class))).thenReturn(Optional.of(Role.builder().roleName(AccountRole.ROLE_ADMIN).build()));

        AccountInfo updatedUser = generateUserInfo(true, false);
        updatedUser.setUsername("updatedUsername");
        when(accountInfoRepository.save(any(AccountInfo.class))).thenReturn(updatedUser);

        // Act
        AccountInfoDto result = accountInfoService.updateAccount(existingUser.getAccountId(), existingUserDto);

        // Assert
        assertThat(result.getUsername()).isEqualTo(updatedUser.getUsername());
    }

    @Test
    void getUserWhenUserIsPresent() {
        // Arrange
        AccountInfo existingUser = generateUserInfo(true, false);
        when(accountInfoRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));

        // Act
        AccountInfoDto result = accountInfoService.getAccount(existingUser.getAccountId());

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(existingUser.getUsername());
        assertThat(result.getPassword()).isNull();
    }

    @Test
    void getAllUsers() {
        // Arrange
        List<AccountInfo> users = List.of(generateUserInfo(true, true), generateUserInfo(false, true));
        when(accountInfoRepository.findAll()).thenReturn(users);

        // Act
        List<AccountInfoDto> result = accountInfoService.getAllAccounts();

        // Assert
        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo(users.get(0).getUsername());
        assertThat(result.get(1).getUsername()).isEqualTo(users.get(1).getUsername());
    }

    @Test
    void deleteUser() {
        // Act
        accountInfoService.deleteAccount(5L);

        // Assert
        verify(accountInfoRepository).deleteById(5L);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "1234", "pass", "abcd", "password", "12345678", "qwerty", "admin", "111222333444"})
    void isInsecurePasswordShort(String password) {
        // Arrange
        AccountInfoDto accountInfoDto = generateUserInfoDto(true, false);
        accountInfoDto.setPassword(password);

        // Act & Assert
        assertThatThrownBy(() -> accountInfoService.addAccount(accountInfoDto))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("Password must be at least");
    }

    @Test
    void isInsecurePasswordSecure() {
        String password = "LongEnough1$";

        boolean result = accountInfoService.isInsecurePassword(password);

        assertThat(result).isFalse();
    }

    


    private AccountInfo generateUserInfo(boolean isUser, boolean isAdmin) {
        var user = AccountInfo.builder()
                .accountId(5L)
                .username("username")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .email("email@stapledon.ca")
                .build();
        if (isUser) {
            user.getRoles().add(Role.builder().roleName(AccountRole.ROLE_USER).build());
        }
        if (isAdmin) {
            user.getRoles().add(Role.builder().roleName(AccountRole.ROLE_ADMIN).build());
        }
        return user;
    }
    private AccountInfoDto generateUserInfoDto(boolean isUser, boolean isAdmin) {
        var user = AccountInfoDto.builder()
                .username("username")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .email("email@stapledon.ca")
                .password("LongEnough1$")
                .build();
        if (isUser) {
            user.setRoles(Set.of(AccountRole.ROLE_USER.toString()));
        }
        if (isAdmin) {
            user.setRoles(Set.of(AccountRole.ROLE_ADMIN.toString()));
        }
        return user;
    }
}