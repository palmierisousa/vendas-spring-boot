package io.github.palmierisousa.service.impl;

import io.github.palmierisousa.api.dto.UserDTO;
import io.github.palmierisousa.domain.entity.User;
import io.github.palmierisousa.domain.repository.Users;
import io.github.palmierisousa.exception.ElementAlreadyExists;
import io.github.palmierisousa.exception.ElementNotFoundException;
import io.github.palmierisousa.exception.PasswordInvalidException;
import io.github.palmierisousa.security.jwt.JwtService;
import io.github.palmierisousa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private Users repository;

    @Autowired
    private JwtService jwtService;

    @Override
    @Transactional
    public User save(UserDTO user) {
        boolean exists = repository.existsByLogin(user.getLogin());

        if (exists) {
            throw new ElementAlreadyExists("Usuário já cadastrado: " + user.getLogin());
        }

        String encodedPassword = encoder.encode(user.getPassword());

        return repository.save(User.builder().login(user.getLogin()).password(encodedPassword).admin(false).build());
    }

    @Override
    public String authenticate(UserDTO credentials) {
        UserDetails user = loadUserByUsername(credentials.getLogin());
        boolean matched = encoder.matches(credentials.getPassword(), user.getPassword());

        if (matched) {
            User u = User.builder().login(credentials.getLogin()).password(credentials.getPassword())
                    .build();

            return jwtService.generateToken(u);
        }

        throw new PasswordInvalidException();
    }

    @Override
    @Transactional
    public void updateAdmin(String login) {
        User userFounded = repository.findByLogin(login)
                .map(user -> {
                    user.setAdmin(!user.getAdmin());
                    repository.save(user);

                    return user;
                })
                .orElseThrow(() -> new ElementNotFoundException("Usuário inexistente: " + login));
    }

    @Override
    @Transactional
    public List<User> filter(UserDTO filter) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example<User> example = Example.of(
                User.builder().login(filter.getLogin()).admin(filter.getAdmin()).build(),
                matcher);
        return repository.findAll(example);
    }

    @Override
    public User get(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new ElementNotFoundException("Usuário inexistente: " + login));
    }

    @Override
    @Transactional
    public void delete(String login) {
        repository.findByLogin(login).map(u -> {
            repository.delete(u);
            return Void.TYPE;
        }).orElseThrow(() -> new ElementNotFoundException("Usuário inexistente: " + login));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados"));

        String[] roles = user.getAdmin() ? new String[]{ "ADMIN", "USER" } : new String[]{ "USER" };

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }
}
