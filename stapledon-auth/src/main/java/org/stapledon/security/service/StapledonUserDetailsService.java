package org.stapledon.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.stapledon.security.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class StapledonUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username)
                .map(StapledonUserDetails::build)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }
}
