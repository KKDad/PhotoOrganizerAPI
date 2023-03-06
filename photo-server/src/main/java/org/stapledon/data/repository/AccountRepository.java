package org.stapledon.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stapledon.data.entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
