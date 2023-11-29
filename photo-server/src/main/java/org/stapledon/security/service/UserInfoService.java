package org.stapledon.security.service;

import jdk.jfr.Label;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
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
    private PasswordEncoder encoder;

    @Override
    public UserInfoDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByUsername(username);
        return mapper.toUserInfoDetails(userDetail.orElseThrow(() -> new UsernameNotFoundException("User not found " + username)));
    }

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }
}
