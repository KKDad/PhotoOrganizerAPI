package org.stapledon.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.filter.UserInfoDetails;
import org.stapledon.security.mapper.AccountMapper;
import org.stapledon.security.repository.UserInfoRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository repository;
    private final AccountMapper mapper;

    @Lazy
    @Autowired
    // Do not use remove @Autowired and use constructor injection - Will cause circular dependency
    private PasswordEncoder encoder;

    @Override
    public UserInfoDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByUsername(username);
        return mapper.toUserInfoDetails(userDetail.orElseThrow(() -> new UsernameNotFoundException("User not found " + username)));
    }

    @Transactional
    public UserInfo addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        return repository.save(userInfo);
    }
}
