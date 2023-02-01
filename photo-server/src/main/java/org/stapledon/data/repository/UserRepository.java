package org.stapledon.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stapledon.data.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
