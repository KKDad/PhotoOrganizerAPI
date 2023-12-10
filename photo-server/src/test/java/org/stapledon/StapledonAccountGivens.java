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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.stapledon.data.FolderService;
import org.stapledon.entities.Folder;
import org.stapledon.security.entities.AccountInfo;
import org.stapledon.security.entities.Role;
import org.stapledon.security.entities.enums.AccountRole;
import org.stapledon.security.repository.AccountInfoRepository;
import org.stapledon.security.repository.RoleRepository;
import org.stapledon.security.service.JwtService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@Slf4j
@Component
@RequiredArgsConstructor
public class StapledonAccountGivens implements ApplicationContextAware {

    private final Clock clock;
    private final AtomicInteger counter = new AtomicInteger(1000);


    public void useDefaultClock() {
        doAnswer(invocation -> Instant.now()).when(clock).instant();
        doReturn(ZoneOffset.UTC).when(clock).getZone();
    }

    @Transactional
    public void clearAccounts() {
        log.info("Clearing accounts from database");
        accountInfoRepository.deleteAll();
    }

    @Transactional
    public void addDefaultRoles() {
        log.info("Adding default roles");
        if (roleRepository.findByRoleName(AccountRole.ROLE_USER).isEmpty()) {
            roleRepository.save(
                    Role.builder()
                            .roleName(AccountRole.ROLE_USER)
                            .build());
        }
        if (roleRepository.findByRoleName(AccountRole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(
                    Role.builder()
                            .roleName(AccountRole.ROLE_ADMIN)
                            .build());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        StapledonAccountGivens.applicationContext = applicationContext;
    }
    private static ApplicationContext applicationContext;
    protected static JwtService jwtService() {
        return applicationContext.getBean(JwtService.class);
    }


    // User Givens
    @Builder
    @Getter
    @Accessors(fluent = true)
    public static class AccountInfoParameters {
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
        private final Set<AccountRole> roles = Set.of(AccountRole.ROLE_USER);
    }
    @Builder
    @Getter
    @Accessors(fluent = true)
    public static class GivenAccountContext {
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
    private final AccountInfoRepository accountInfoRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public GivenAccountContext givenAdministrator() {
        return givenUser(AccountInfoParameters.builder()
                .username("admin")
                .email("admin@stapledon.ca")
                .roles(Set.of(AccountRole.ROLE_ADMIN))
                .build());
    }

    public GivenAccountContext givenUser() {
        return givenUser(AccountInfoParameters
                .builder()
                .email("user@stapledon.ca")
                .roles(Set.of(AccountRole.ROLE_USER))
                .build());
    }

    @Transactional
    public GivenAccountContext givenUser(AccountInfoParameters parameters) {
        parameters.roles.forEach(role -> {
            if (roleRepository.findByRoleName(Enum.valueOf(AccountRole.class, role.name())).isEmpty()) {
                log.info("Adding role {}", role.name());
                roleRepository.saveAll(List.of(Role.builder()
                        .roleName(Enum.valueOf(AccountRole.class, role.name()))
                        .build()));
            }
        });
        var user = AccountInfo.builder()
                .username(parameters.username())
                .password(encoder.encode(parameters.password()))
                .firstName(parameters.firstName())
                .lastName(parameters.lastName())
                .email(parameters.email())
                .roles(parameters.roles.stream()
                        .map(role -> roleRepository.findByRoleName(Enum.valueOf(AccountRole.class, role.name())).get())
                        .collect(java.util.stream.Collectors.toSet())
                )
                .build();

        var entity = accountInfoRepository.save(user);
        return GivenAccountContext.builder()
                .id(entity.getAccountId())
                .username(entity.getUsername())
                .password(parameters.password)
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .roles(Set.of(Role.builder()
                        .roleName(AccountRole.ROLE_USER)
                        .build()))
                .build();
    }

    @Builder
    @Getter
    @Accessors(fluent = true)
    public static class FolderParameters {
        private final String name;
        private final String date;
        @Builder.Default
        private final List<FolderParameters> childFolders = new ArrayList<>();
    }

    @Builder
    @Getter
    @Accessors(fluent = true)
    public static class FolderContext {
        private final String name;
        private final LocalDate date;
        private final List<FolderContext> childFolders;
    }
    @SuppressWarnings("unchecked")
    public List<FolderContext> givenFolders(List<FolderParameters> parameters) {
        List<Folder> folderCache = parameters.stream().map(f -> Folder.builder()
                .name(f.name())
                .date(Date.from(LocalDate.parse(f.date()).atStartOfDay().toInstant(ZoneOffset.UTC)))
                .childFolders(new ArrayList<>())
                .build()).toList();
        // Get around the fact that the folder service is a singleton
        var c = (List<Folder>)ReflectionTestUtils.getField(FolderService.class, "folderCache");
       assertThat(c).isNotNull();
        c.clear();
        c.addAll(folderCache);

        return folderCache.stream().flatMap(f -> {
            var folder = FolderContext.builder()
                    .name(f.getName())
                    .date(f.getDate().toInstant().atZone(ZoneOffset.UTC).toLocalDate())
                    .childFolders(new ArrayList<>())
                    .build();
            if (f.getChildFolders() != null) {
                f.getChildFolders().forEach(cf -> {
                    var childFolder = FolderContext.builder()
                            .name(cf.getName())
                            .date(cf.getDate().toInstant().atZone(ZoneOffset.UTC).toLocalDate())
                            .childFolders(new ArrayList<>())
                            .build();
                    folder.childFolders().add(childFolder);
                });
            }
            return List.of(folder).stream();
        }).toList();
    }

    public FolderContext givenFolder(FolderParameters parameters) {
        return FolderContext.builder()
                .name(parameters.name())
                .date(LocalDate.parse(parameters.date()))
                .childFolders(parameters.childFolders().stream()
                        .map(this::folder)
                        .collect(java.util.stream.Collectors.toList()))
                .build();
    }
    private FolderContext folder(FolderParameters parameters) {
        return FolderContext.builder()
                .name(parameters.name())
                .date(LocalDate.parse(parameters.date()))
                .childFolders(parameters.childFolders().stream()
                        .map(this::folder)
                        .collect(java.util.stream.Collectors.toList()))
                .build();
    }
}
