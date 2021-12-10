package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.dto.TokenDTO;
import io.github.palmierisousa.api.dto.UserDTO;
import io.github.palmierisousa.domain.entity.User;
import io.github.palmierisousa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public UserDTO save(@RequestBody @Valid UserDTO user) {
        User u = service.save(user);

        return UserDTO.builder().login(u.getLogin()).admin(u.getAdmin()).build();
    }

    @PostMapping("/auth")
    public TokenDTO authenticate(@RequestBody UserDTO credentials) {
        String token = service.authenticate(credentials);

        return TokenDTO.builder().login(credentials.getLogin()).token(token).build();
    }

    @PatchMapping("{login}")
    @ResponseStatus(NO_CONTENT)
    public void updateAdmin(@PathVariable String login) {
        service.updateAdmin(login);
    }

    @GetMapping("{login}")
    public UserDTO getByLogin(@PathVariable String login) {
        User user = service.get(login);

        return UserDTO.builder().login(user.getLogin()).admin(user.getAdmin()).build();
    }

    @GetMapping
    public List<UserDTO> find(UserDTO filter) {
        List<User> users = service.filter(filter);

        return users.stream().map(
                u -> UserDTO.builder().login(u.getLogin()).admin(u.getAdmin()).build()
        ).collect(Collectors.toList());
    }

    @DeleteMapping("{login}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable String login) {
        service.delete(login);
    }

}
