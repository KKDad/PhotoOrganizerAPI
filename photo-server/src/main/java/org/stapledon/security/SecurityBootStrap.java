package org.stapledon.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.stapledon.security.dto.AccountInfoDto;
import org.stapledon.security.entities.AccountInfo;
import org.stapledon.security.entities.Role;
import org.stapledon.security.entities.enums.AccountRole;
import org.stapledon.security.mapper.AccountInfoMapper;
import org.stapledon.security.repository.AccountInfoRepository;

import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityBootStrap {

    private final AccountInfoRepository accountInfoRepository;
    private final AccountInfoMapper mapper;

    @Value("${photo.security.autoboot.enabled}")
    private boolean autoBootEnabled;

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void createAdministratorAccount() {
        if (autoBootEnabled && Boolean.FALSE.equals(accountInfoRepository.existsByUsername("administrator"))) {
            log.warn("No administrator account found. Creating default administrator account.");
            var admin = AccountInfoDto.builder()
                    .username("administrator")
                    .password("admin123")
                    .firstName("Administrator")
                    .lastName("Administrator")
                    .email("admin@stapledon.ca")
                    .roles(Set.of(AccountRole.ROLE_ADMIN.name()))
                    .build();

            accountInfoRepository.save(mapper.toAccountInfo(admin));
        }
    }
}

