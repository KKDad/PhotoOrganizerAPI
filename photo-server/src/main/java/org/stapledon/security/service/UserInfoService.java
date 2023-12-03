package org.stapledon.security.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.stapledon.InvalidParameterException;
import org.stapledon.security.dto.UserInfoDto;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.entities.enums.UserRole;
import org.stapledon.security.filter.UserInfoDetails;
import org.stapledon.security.mapper.UserInfoMapper;
import org.stapledon.security.repository.RoleRepository;
import org.stapledon.security.repository.UserInfoRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository repository;
    private final RoleRepository roleRepository;
    private final UserInfoMapper mapper;

    @Value("${security.min-password-length:8}")
    private Integer minPasswordLength;


    @Override
    @Transactional(readOnly = true)
    public UserInfoDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByUsername(username);
        return mapper.toSecurityUserInfoDetails(userDetail.orElseThrow(() -> new UsernameNotFoundException("User not found " + username)));
    }

    @Transactional
    public UserInfoDto addUser(UserInfoDto userInfo) {
        if (isInsecurePassword(userInfo.getPassword())) {
            throw new InvalidParameterException("Password must be at least " + minPasswordLength + " characters and contain at least one uppercase letter, one lowercase letter, one digit and one special character");
        }

        if (repository.findByUsername(userInfo.getUsername()).isPresent()) {
            throw new InvalidParameterException("User already exists");
        }
        UserInfo toSave = mapper.toUserInfo(userInfo);
        toSave.setRoles(userInfo.getRoles().stream()
                .map(role -> roleRepository.findByRoleName(Enum.valueOf(UserRole.class, role))
                .orElseThrow(() -> new InvalidParameterException("Role not found: " + role)))
                .collect(java.util.stream.Collectors.toSet())
        );
        UserInfo savedUser = repository.save(toSave);
        return mapper.toDto(savedUser);
    }


    @Transactional
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public UserInfoDto updateUser(Long id, UserInfoDto userInfo) {
        if (!Strings.isEmpty(userInfo.getPassword()) && isInsecurePassword(userInfo.getPassword())) {
            throw new InvalidParameterException("Password must be at least " + minPasswordLength + " characters and contain at least one uppercase letter, one lowercase letter, one digit and one special character");
        }
        Optional<UserInfo> user = repository.findById(id);
        if (user.isPresent()) {
            UserInfo updatedUser = user.get();
            mapper.merge(updatedUser, userInfo);
            UserInfo savedUser = repository.save(updatedUser);
            return mapper.toDto(savedUser);
        } else {
            throw new InvalidParameterException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public UserInfoDto getUser(Long id) {
        Optional<UserInfo> user = repository.findById(id);
        if (user.isPresent()) {
            return mapper.toDto(user.get());
        } else {
            throw new InvalidParameterException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public List<UserInfoDto> getAllUsers() {
       return repository.findAll().stream().map(mapper::toDto).toList();
    }

    /**
     * Checks if a password is secure based on certain rules.
     *
     * @param password The password to check
     * @return true if the password is secure, false otherwise
     */
    public boolean isInsecurePassword(String password) {
        // Check for minimum length
        if (password.length() < minPasswordLength) {
            return true;
        }

        // Check for at least one uppercase letter, one lowercase letter, one digit and one special character
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(password).matches()) {
            return true;
        }

        // Check for sequences or repetitions
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) && password.charAt(i) == password.charAt(i + 2)) {
                return true;
            }
        }

        // If all checks pass, the password is secure
        return false;
    }
}
