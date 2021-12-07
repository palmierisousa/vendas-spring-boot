package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.dto.UserDTO;
import io.github.palmierisousa.domain.entity.User;
import io.github.palmierisousa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public User save(@RequestBody @Valid UserDTO user) {
        return userService.save(user);
    }

}
