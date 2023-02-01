package org.stapledon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stapledon.data.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

}
