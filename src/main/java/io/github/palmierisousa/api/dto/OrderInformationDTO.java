package io.github.palmierisousa.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInformationDTO {
    @JsonProperty("number")
    private Integer orderId;

    private String cpf;

    @JsonProperty("client_name")
    private String clientName;

    private BigDecimal total;

    @JsonProperty("order_date")
    private String orderDate;

    private String status;
    
    private List<OrderItemInformationsDTO> items;
}