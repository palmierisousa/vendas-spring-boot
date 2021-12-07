package io.github.palmierisousa.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusDTO {
    @JsonProperty("new_status")
    private String newStatus;
}
