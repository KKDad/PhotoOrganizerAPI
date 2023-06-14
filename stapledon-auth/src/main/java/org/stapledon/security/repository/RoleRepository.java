package org.stapledon.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stapledon.security.domain.Role;
import org.stapledon.security.domain.enums.AccountRole;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AccountRole name);
}