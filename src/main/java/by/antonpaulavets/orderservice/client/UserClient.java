package by.antonpaulavets.orderservice.client;


import by.antonpaulavets.orderservice.dto.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {

    @GetMapping("/users/by-email")
    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "fallbackUser")
    UserDto getUserByEmail(@RequestParam String email);

    default UserDto fallbackUser(String email, Throwable ex) {
        UserDto user = new UserDto();
        user.setEmail(email);
        user.setFirstName("Unknown");
        user.setLastName("User");
        return user;
    }
}
