package by.antonpaulavets.orderservice.integrationtest;

import by.antonpaulavets.orderservice.dto.OrderDto;
import by.antonpaulavets.orderservice.dto.OrderItemDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.springframework.http.*;

import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import java.util.List;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    @Container
    static WireMockContainer userMock =
            new WireMockContainer("wiremock/wiremock:3.5.2");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeAll
    static void setup() {
        // Подставляем URL в Feign Client
        System.setProperty("user.service.url", userMock.getBaseUrl());

        // Мокаем вызов UserService
        userMock.stubFor(get(urlPathEqualTo("/users/by-email"))
                .withQueryParam("email", equalTo("test@mail.com"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("""
                                {
                                    "id": 1,
                                    "email": "test@mail.com",
                                    "firstName": "John",
                                    "lastName": "Doe"
                                }
                                """)));
    }

    @Test
    void testCreateOrder() throws Exception {

        OrderItemDto item = new OrderItemDto();
        item.setItemId(1L);
        item.setQuantity(2);

        OrderDto dto = new OrderDto();
        dto.setUserId(1L);
        dto.setStatus("NEW");
        dto.setOrderItems(List.of(item));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.order.status").value("NEW"))
                .andExpect(jsonPath("$.user.email").value("test@mail.com"));
    }
}