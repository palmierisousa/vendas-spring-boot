package io.github.palmierisousa.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemInformationsDTO {
    @JsonProperty("product_description")
    private String productDescription;

    @JsonProperty("unit_price")
    private BigDecimal unitPrice;

    private Integer amount;

    private Integer code;
}