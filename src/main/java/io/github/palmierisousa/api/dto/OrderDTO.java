package io.github.palmierisousa.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.palmierisousa.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    @NotNull(message = "{field.code-client.required}")
    @JsonProperty("client")
    private Integer clientId;

    @NotNull(message = "{field.total-order.required}")
    private BigDecimal total;

    @NotEmptyList(message = "{field.items-order.required}")
    private List<OrderItemDTO> items;
}
