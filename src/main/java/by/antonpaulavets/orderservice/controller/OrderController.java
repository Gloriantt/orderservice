package by.antonpaulavets.orderservice.controller;
import by.antonpaulavets.orderservice.dto.OrderDto;
import by.antonpaulavets.orderservice.dto.OrderResponseDto;
import by.antonpaulavets.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto createOrder(@Valid @RequestBody OrderDto order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}")
    public OrderResponseDto updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDto order) {
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @GetMapping("/status")
    public List<OrderResponseDto> getOrdersByStatuses(@RequestParam List<String> statuses) {
        return orderService.getOrdersByStatuses(statuses);
    }
}