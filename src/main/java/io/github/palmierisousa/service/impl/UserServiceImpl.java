package io.github.palmierisousa.service.impl;

import io.github.palmierisousa.api.dto.UserDTO;
import io.github.palmierisousa.domain.entity.User;
import io.github.palmierisousa.domain.repository.Users;
import io.github.palmierisousa.exception.PasswordInvalidException;
import io.github.palmierisousa.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements io.github.palmierisousa.service.UserService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private Users userRepository;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public User save(UserDTO user) {
        User u = new User();
        u.setLogin(user.getLogin());
        u.setAdmin(user.isAdmin());

        String encodedPassword = encoder.encode(user.getPassword());
        u.setPassword(encodedPassword);

        return userRepository.save(u);
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados"));

        String[] roles = user.isAdmin() ? new String[]{ "ADMIN", "USER" } : new String[]{ "USER" };

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }
}
