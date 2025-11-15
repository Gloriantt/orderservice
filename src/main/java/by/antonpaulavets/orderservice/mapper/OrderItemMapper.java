package by.antonpaulavets.orderservice.mapper;

import by.antonpaulavets.orderservice.dto.OrderItemDto;
import by.antonpaulavets.orderservice.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem entity);
    OrderItem toEntity(OrderItemDto dto);
}
