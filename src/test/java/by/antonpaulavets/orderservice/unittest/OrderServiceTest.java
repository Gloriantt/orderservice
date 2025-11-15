package by.antonpaulavets.orderservice.unittest;

import by.antonpaulavets.orderservice.client.UserClient;
import by.antonpaulavets.orderservice.dto.OrderDto;
import by.antonpaulavets.orderservice.dto.OrderResponseDto;
import by.antonpaulavets.orderservice.dto.UserDto;
import by.antonpaulavets.orderservice.exception.OrderNotFoundException;
import by.antonpaulavets.orderservice.mapper.OrderMapper;
import by.antonpaulavets.orderservice.model.Order;
import by.antonpaulavets.orderservice.repository.OrderRepository;
import by.antonpaulavets.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private UserClient userClient;
    private OrderMapper orderMapper;

    private OrderService orderService;

    @BeforeEach
    void setup() {
        orderRepository = mock(OrderRepository.class);
        userClient = mock(UserClient.class);
        orderMapper = mock(OrderMapper.class);

        orderService = new OrderService(orderRepository, userClient, orderMapper);
    }

    @Test
    void testCreateOrder() {
        OrderDto dto = new OrderDto();
        dto.setStatus("NEW");

        Order entity = new Order();
        entity.setId(1L);

        OrderDto dtoMapped = new OrderDto();
        dtoMapped.setId(1L);

        UserDto user = new UserDto();
        user.setEmail("test@mail.com");
        user.setFirstName("John");

        when(orderMapper.toEntity(dto)).thenReturn(entity);
        when(orderRepository.save(entity)).thenReturn(entity);
        when(orderMapper.toDto(entity)).thenReturn(dtoMapped);
        when(userClient.getUserByEmail("test@mail.com")).thenReturn(user);

        OrderResponseDto response = orderService.createOrder(dto);

        assertThat(response.getOrder().getId()).isEqualTo(1L);
        assertThat(response.getUser().getEmail()).isEqualTo("test@mail.com");
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        order.setId(1L);

        OrderDto dtoMapped = new OrderDto();
        dtoMapped.setId(1L);

        UserDto user = new UserDto();
        user.setEmail("test@mail.com");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(dtoMapped);
        when(userClient.getUserByEmail("test@mail.com")).thenReturn(user);

        OrderResponseDto response = orderService.getOrderById(1L);

        assertThat(response.getOrder().getId()).isEqualTo(1L);
        assertThat(response.getUser().getEmail()).isEqualTo("test@mail.com");
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(99L))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void testUpdateOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("OLD");

        OrderDto dtoUpdate = new OrderDto();
        dtoUpdate.setStatus("NEW");

        OrderDto dtoMapped = new OrderDto();
        dtoMapped.setId(1L);
        dtoMapped.setStatus("NEW");

        UserDto user = new UserDto();
        user.setEmail("test@mail.com");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(dtoMapped);
        when(userClient.getUserByEmail("test@mail.com")).thenReturn(user);

        OrderResponseDto response = orderService.updateOrder(1L, dtoUpdate);

        assertThat(response.getOrder().getStatus()).isEqualTo("NEW");
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetOrdersByStatuses() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("NEW");

        OrderDto dtoMapped = new OrderDto();
        dtoMapped.setId(1L);
        dtoMapped.setStatus("NEW");

        UserDto user = new UserDto();
        user.setEmail("test@mail.com");

        when(orderRepository.findByStatusIn(List.of("NEW"))).thenReturn(List.of(order));
        when(orderMapper.toDto(order)).thenReturn(dtoMapped);
        when(userClient.getUserByEmail("test@mail.com")).thenReturn(user);

        List<OrderResponseDto> list = orderService.getOrdersByStatuses(List.of("NEW"));

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getOrder().getId()).isEqualTo(1L);
    }
}