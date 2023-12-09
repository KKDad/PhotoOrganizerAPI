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
import org.stapledon.security.dto.UserInfoDto;
import org.stapledon.security.entities.Role;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.entities.enums.UserRole;
import org.stapledon.security.filter.UserInfoDetails;
import org.stapledon.security.mapper.UserInfoMapper;
import org.stapledon.security.repository.RoleRepository;
import org.stapledon.security.repository.UserInfoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @Mock
    private UserInfoRepository userInfoRepository;
    @Mock
    private RoleRepository roleRepository;
    @Spy
    private UserInfoMapper mapper;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    UserInfoService userInfoService;

   @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mapper, "encoder", encoder);
        ReflectionTestUtils.setField(userInfoService, "minPasswordLength", 2);
    }

    @Test
    void loadUserByUsernameSuccess() {
       UserInfo userInfo = generateUserInfo(false, true);
        when(userInfoRepository.findByUsername(anyString())).thenReturn(
                Optional.of(userInfo));

        UserInfoDetails result = userInfoService.loadUserByUsername("username");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userInfo.getUsername());
        assertThat(result.getPassword()).isEqualTo(userInfo.getPassword());
        assertThat(result.getAuthorities()).isNotEmpty().hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
        assertThat(result.isEnabled()).isTrue();
        assertThat(result.isAccountNonExpired()).isTrue();
        assertThat(result.isAccountNonLocked()).isTrue();
        assertThat(result.isCredentialsNonExpired()).isTrue();
    }


    @Test
    void addUser() {
        UserInfoDto userInfoDto = generateUserInfoDto(false, true);
        UserInfo userInfo = generateUserInfo(false, true);

        when(userInfoRepository.save(any(UserInfo.class))).thenReturn(userInfo);
        when(roleRepository.findByRoleName(any(UserRole.class))).thenReturn(Optional.of(Role.builder().roleName(UserRole.ROLE_ADMIN).build()));
        when(userInfoRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(encoder.encode(any())).thenReturn("password");

        UserInfoDto result = userInfoService.addUser(userInfoDto);

        verify(userInfoRepository).findByUsername(anyString());
        verify(roleRepository).findByRoleName(any(UserRole.class));
        verify(userInfoRepository).save(any(UserInfo.class));
        verify(encoder).encode(any());

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userInfo.getUsername());
        assertThat(result.getPassword()).isNull();
        assertThat(result.getRoles()).isNotEmpty().hasSize(1);
        assertThat(result.getRoles().iterator().next()).isEqualTo(UserRole.ROLE_ADMIN.toString());
    }

    @Test
    void addUserInsecurePassword() {
        UserInfoDto userInfoDto = generateUserInfoDto(true, false);
        userInfoDto.setPassword("short");

        assertThatThrownBy(() -> userInfoService.addUser(userInfoDto))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("Password must be at least");
    }

    @Test
    void addUserUsernameExists() {
        UserInfoDto userInfoDto = generateUserInfoDto(true, false);
        UserInfo userInfo = generateUserInfo(true, false);
        when(userInfoRepository.findByUsername(anyString())).thenReturn(Optional.of(userInfo));

        assertThatThrownBy(() -> userInfoService.addUser(userInfoDto))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("User already exists");
    }

    @Test
    void updateUserInsecurePassword() {
        UserInfoDto userInfoDto = generateUserInfoDto(true, false);
        userInfoDto.setPassword("short");

        assertThatThrownBy(() -> userInfoService.updateUser(1L, userInfoDto))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("Password must be at least");
    }

    @Test
    void updateUserNotFound() {
        UserInfoDto userInfoDto = generateUserInfoDto(true, false);
        when(userInfoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userInfoService.updateUser(1L, userInfoDto))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("User not found");
    }

    @Test
    void getUserNotFound() {
        when(userInfoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userInfoService.getUser(1L))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("User not found");
    }

    @Test
    void updateUserTest() {
        // Arrange
        UserInfo existingUser = generateUserInfo(true, false);
        UserInfoDto existingUserDto = generateUserInfoDto(true, false);
        when(userInfoRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(encoder.encode(any())).thenReturn("password");
        when(roleRepository.findByRoleName(any(UserRole.class))).thenReturn(Optional.of(Role.builder().roleName(UserRole.ROLE_ADMIN).build()));

        UserInfo updatedUser = generateUserInfo(true, false);
        updatedUser.setUsername("updatedUsername");
        when(userInfoRepository.save(any(UserInfo.class))).thenReturn(updatedUser);

        // Act
        UserInfoDto result = userInfoService.updateUser(existingUser.getUserId(), existingUserDto);

        // Assert
        assertThat(result.getUsername()).isEqualTo(updatedUser.getUsername());
    }

    @Test
    void getUserWhenUserIsPresent() {
        // Arrange
        UserInfo existingUser = generateUserInfo(true, false);
        when(userInfoRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));

        // Act
        UserInfoDto result = userInfoService.getUser(existingUser.getUserId());

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(existingUser.getUsername());
        assertThat(result.getPassword()).isNull();
    }

    @Test
    void getAllUsers() {
        // Arrange
        List<UserInfo> users = List.of(generateUserInfo(true, true), generateUserInfo(false, true));
        when(userInfoRepository.findAll()).thenReturn(users);

        // Act
        List<UserInfoDto> result = userInfoService.getAllUsers();

        // Assert
        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo(users.get(0).getUsername());
        assertThat(result.get(1).getUsername()).isEqualTo(users.get(1).getUsername());
    }

    @Test
    void deleteUser() {
        // Act
        userInfoService.deleteUser(5L);

        // Assert
        verify(userInfoRepository).deleteById(5L);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "1234", "pass", "abcd", "password", "12345678", "qwerty", "admin", "111222333444"})
    void isInsecurePasswordShort(String password) {
        // Arrange
        UserInfoDto userInfoDto = generateUserInfoDto(true, false);
        userInfoDto.setPassword(password);

        // Act & Assert
        assertThatThrownBy(() -> userInfoService.addUser(userInfoDto))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessageContaining("Password must be at least");
    }

    @Test
    void isInsecurePasswordSecure() {
        String password = "LongEnough1$";

        boolean result = userInfoService.isInsecurePassword(password);

        assertThat(result).isFalse();
    }

    


    private UserInfo generateUserInfo(boolean isUser, boolean isAdmin) {
        var user = UserInfo.builder()
                .userId(5L)
                .username("username")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .email("email@stapledon.ca")
                .build();
        if (isUser) {
            user.getRoles().add(Role.builder().roleName(UserRole.ROLE_USER).build());
        }
        if (isAdmin) {
            user.getRoles().add(Role.builder().roleName(UserRole.ROLE_ADMIN).build());
        }
        return user;
    }
    private UserInfoDto generateUserInfoDto(boolean isUser, boolean isAdmin) {
        var user = UserInfoDto.builder()
                .username("username")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .email("email@stapledon.ca")
                .password("LongEnough1$")
                .build();
        if (isUser) {
            user.setRoles(Set.of(UserRole.ROLE_USER.toString()));
        }
        if (isAdmin) {
            user.setRoles(Set.of(UserRole.ROLE_ADMIN.toString()));
        }
        return user;
    }
}