package org.stapledon.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stapledon.data.mapper.AccountMapper;
import org.stapledon.data.model.AccountAto;
import org.stapledon.data.repository.AccountRepository;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    public List<AccountAto> fetchAll() {
        return Collections.emptyList();
    }

    public AccountAto fetch(Long id) {
        var user = accountRepository.findById(id);
        return user.map(accountMapper::toAto).orElseThrow();
    }

    @Transactional
    public AccountAto save(AccountAto accountAto) {
        log.info("Saving account={}", accountAto);
        var toSave = accountMapper.toModel(accountAto);
        var saved = accountRepository.save(toSave);
        return accountMapper.toAto(saved);
    }
}
