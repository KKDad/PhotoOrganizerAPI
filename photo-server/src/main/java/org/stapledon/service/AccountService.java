package org.stapledon.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stapledon.data.entities.Account;
import org.stapledon.data.mapper.AccountMapper;
import org.stapledon.data.model.AccountAto;
import org.stapledon.data.repository.AccountRepository;

import java.util.Collections;
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
    public void save(AccountAto accountAto) {
        log.info("Saving account={}", accountAto);
        accountRepository.save(accountMapper.toModel(accountAto));
    }

    public AccountAto fetchByUsername(String username) {
        Optional<Account> user = accountRepository.findByUsername(username);
        return user.map(accountMapper::toAto).orElseThrow();
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}
