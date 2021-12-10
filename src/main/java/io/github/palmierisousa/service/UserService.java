package io.github.palmierisousa.service;

import io.github.palmierisousa.api.dto.UserDTO;
import io.github.palmierisousa.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User save(UserDTO user);

    String authenticate(UserDTO credentials);

    void updateAdmin(String login);

    List<User> filter(UserDTO filter);

    User get(String login);

    void delete(String login);
}

