package com.cocoh.movie.service;

import com.cocoh.movie.common.Role;
import com.cocoh.movie.dto.AddUserRequestDto;
import com.cocoh.movie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.cocoh.movie.Entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String save(AddUserRequestDto dto) {
        return userRepository.save(User.builder()
                .username(dto.getUsername())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(Role.ROLE_ADMIN)
                .build()).getUsername();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}
