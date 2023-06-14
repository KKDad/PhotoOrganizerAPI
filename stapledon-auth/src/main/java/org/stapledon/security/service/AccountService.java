package org.stapledon.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stapledon.security.domain.Account;
import org.stapledon.security.mapper.AccountMapper;
import org.stapledon.security.model.AccountAto;
import org.stapledon.security.repository.AccountRepository;


import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    public List<AccountAto> fetchAll() {
        return accountRepository
                .findAll()
                .stream()
                .map(accountMapper::toAto)
                .toList();
    }

    public AccountAto fetch(Long id) {
        var user = accountRepository.findById(id);
        return user.map(accountMapper::toAto).orElseThrow();
    }

    @Transactional
    public void save(AccountAto accountAto) throws DuplicateAccountException {
        log.info("Saving account={}", accountAto);
        if (Boolean.TRUE.equals(accountRepository.existsByUsername(accountAto.getUsername()))) {
            throw new DuplicateAccountException("Username is already taken");
        }

        if (Boolean.TRUE.equals(accountRepository.existsByEmail(accountAto.getEmail()))) {
            throw new DuplicateAccountException("Email is already in use");
        }
        accountRepository.save(accountMapper.toModel(accountAto));
    }

    public AccountAto fetchByUsername(String username) {
        Optional<Account> user = accountRepository.findByUsername(username);
        return user.map(accountMapper::toAto).orElseThrow();
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    public AccountAto fetchByEmail(String email) {
        Optional<Account> user = accountRepository.findByEmail(email);
        return user.map(accountMapper::toAto).orElseThrow();
    }
}