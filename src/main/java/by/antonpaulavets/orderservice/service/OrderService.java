package by.antonpaulavets.orderservice.service;


import by.antonpaulavets.orderservice.client.UserClient;
import by.antonpaulavets.orderservice.dto.*;
import by.antonpaulavets.orderservice.mapper.OrderMapper;
import by.antonpaulavets.orderservice.model.Order;
import by.antonpaulavets.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private UserClient userClient;
    private OrderMapper orderMapper;

    public OrderResponseDto createOrder(OrderDto dto) {
        Order order = orderMapper.toEntity(dto);
        orderRepository.save(order);
        UserDto user = userClient.getUserByEmail("test@mail.com"); 
        return new OrderResponseDto(orderMapper.toDto(order), user);
    }

    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        UserDto user = userClient.getUserByEmail("test@mail.com");
        return new OrderResponseDto(orderMapper.toDto(order), user);
    }

    @Transactional
    public OrderResponseDto updateOrder(Long id, OrderDto dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(dto.getStatus());
        orderRepository.save(order);
        UserDto user = userClient.getUserByEmail("test@mail.com");
        return new OrderResponseDto(orderMapper.toDto(order), user);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<OrderResponseDto> getOrdersByStatuses(List<String> statuses) {
        return orderRepository.findByStatusIn(statuses)
                .stream()
                .map(order -> {
                    UserDto user = userClient.getUserByEmail("test@mail.com");
                    return new OrderResponseDto(orderMapper.toDto(order), user);
                })
                .collect(Collectors.toList());
    }
}
