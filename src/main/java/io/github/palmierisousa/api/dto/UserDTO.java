package io.github.palmierisousa.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotEmpty(message = "{field.login.required}")
    private String login;

    @NotEmpty(message = "{field.password.required}")
    private String password;

    @JsonProperty("admin")
    private boolean isAdmin;
}
