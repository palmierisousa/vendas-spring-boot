package io.github.palmierisousa.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    @NotNull(message = "{field.code.required}")
    private Integer code;

    @NotEmpty(message = "{field.description.required}")
    private String description;

    @NotNull(message = "{field.price.required}")
    private BigDecimal price;
}
