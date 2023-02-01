package org.stapledon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.stapledon.data.model.User;
import org.stapledon.data.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> fetchAll() {
        return Collections.emptyList();
    }
}
