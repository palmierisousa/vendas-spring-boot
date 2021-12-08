package io.github.palmierisousa.service;

import io.github.palmierisousa.api.dto.TokenDTO;
import io.github.palmierisousa.api.dto.UserDTO;
import io.github.palmierisousa.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserDTO user);

    TokenDTO authenticate(UserDTO credentials);
}
