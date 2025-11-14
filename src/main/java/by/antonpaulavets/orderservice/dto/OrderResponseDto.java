package by.antonpaulavets.orderservice.dto;


import lombok.*;
import org.springframework.stereotype.Service;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderResponseDto {
    private OrderDto order;
    private UserDto user;
}