package org.stapledon;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.stapledon.security.entities.Role;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.entities.enums.UserRole;
import org.stapledon.security.repository.RoleRepository;
import org.stapledon.security.repository.UserInfoRepository;
import org.stapledon.security.service.JwtService;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@Slf4j
@Component
@RequiredArgsConstructor
public class StapledonUserGivens implements ApplicationContextAware {
    private final Clock clock;
    private final AtomicInteger counter = new AtomicInteger(1000);


    public void useDefaultClock() {
        doAnswer(invocation -> Instant.now()).when(clock).instant();
        doReturn(ZoneOffset.UTC).when(clock).getZone();
    }

    @Transactional
    public void clearUsers() {
        log.info("Clearing users from database");
        userInfoRepository.deleteAll();
    }

    @Transactional
    public void addDefaultRoles() {
        log.info("Adding default roles");
        if (roleRepository.findByRoleName(UserRole.ROLE_USER).isEmpty()) {
            roleRepository.save(
                    Role.builder()
                            .roleName(UserRole.ROLE_USER)
                            .build());
        }
        if (roleRepository.findByRoleName(UserRole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(
                    Role.builder()
                            .roleName(UserRole.ROLE_ADMIN)
                            .build());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        StapledonUserGivens.applicationContext = applicationContext;
    }
    private static ApplicationContext applicationContext;
    protected static JwtService jwtService() {
        return applicationContext.getBean(JwtService.class);
    }


    // User Givens
    @Builder
    @Getter
    @Accessors(fluent = true)
    public static class UserInfoParameters {
        @Builder.Default
        private final String username = "user";
        @Builder.Default
        private final String password = "password";
        @Builder.Default
        private final String firstName = "John";
        @Builder.Default
        private final String lastName = "Doe";
        @Builder.Default
        private final String email = "test@stapledon.ca";
        @Builder.Default
        private final Set<UserRole> roles = Set.of(UserRole.ROLE_USER);
    }
    @Builder
    @Getter
    @Accessors(fluent = true)
    public static class GivenUserContext {
        private final Long id;
        private final String username;
        private final String password;
        private final String firstName;
        private final String lastName;
        private final String email;
        private final Set<Role> roles;


        public String authenticate() {
            log.info("Authenticating as {}", username());
            return jwtService().generateToken(username);
        }
    }
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public GivenUserContext givenAdministrator() {
        return givenUser(UserInfoParameters.builder()
                .username("admin")
                .email("admin@stapledon.ca")
                .roles(Set.of(UserRole.ROLE_ADMIN))
                .build());
    }

    public GivenUserContext givenUser() {
        return givenUser(UserInfoParameters
                .builder()
                .email("user@stapledon.ca")
                .roles(Set.of(UserRole.ROLE_USER))
                .build());
    }

    @Transactional
    public GivenUserContext givenUser(UserInfoParameters parameters) {
        parameters.roles.forEach(role -> {
            if (roleRepository.findByRoleName(Enum.valueOf(UserRole.class, role.name())).isEmpty()) {
                log.info("Adding role {}", role.name());
                roleRepository.saveAll(List.of(Role.builder()
                        .roleName(Enum.valueOf(UserRole.class, role.name()))
                        .build()));
            }
        });
        var user = UserInfo.builder()
                .username(parameters.username())
                .password(encoder.encode(parameters.password()))
                .firstName(parameters.firstName())
                .lastName(parameters.lastName())
                .email(parameters.email())
                .roles(parameters.roles.stream()
                        .map(role -> roleRepository.findByRoleName(Enum.valueOf(UserRole.class, role.name())).get())
                        .collect(java.util.stream.Collectors.toSet())
                )
                .build();

        var entity = userInfoRepository.save(user);
        return GivenUserContext.builder()
                .id(entity.getUserId())
                .username(entity.getUsername())
                .password(parameters.password)
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .roles(Set.of(Role.builder()
                        .roleName(UserRole.ROLE_USER)
                        .build()))
                .build();
    }
}
