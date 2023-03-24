package org.stapledon.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stapledon.data.entities.Role;
import org.stapledon.data.entities.enums.AccountRole;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AccountRole name);
}