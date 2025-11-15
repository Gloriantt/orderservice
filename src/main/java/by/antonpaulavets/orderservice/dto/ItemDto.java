package by.antonpaulavets.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "Item name cannot be blank")
    private String name;

    @NotNull
    @Min(value = 0, message = "Price must be positive")
    private Double price;
}