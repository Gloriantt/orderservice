package by.antonpaulavets.orderservice.mapper;

import by.antonpaulavets.orderservice.dto.OrderDto;
import by.antonpaulavets.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDto toDto(Order entity);
    Order toEntity(OrderDto dto);
}
