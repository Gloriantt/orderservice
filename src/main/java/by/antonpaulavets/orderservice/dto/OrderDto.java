package by.antonpaulavets.orderservice.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotEmpty(message = "Status cannot be empty")
    private String status;

    private LocalDateTime creationDate;

    @Valid
    private List<OrderItemDto> orderItems;
}
