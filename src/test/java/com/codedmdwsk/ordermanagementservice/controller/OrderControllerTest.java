package com.codedmdwsk.ordermanagementservice.controller;

import com.codedmdwsk.ordermanagementservice.data.Customer;
import com.codedmdwsk.ordermanagementservice.data.OrderData;
import com.codedmdwsk.ordermanagementservice.dto.OrderCreateDto;
import com.codedmdwsk.ordermanagementservice.dto.OrderListRequestDto;
import com.codedmdwsk.ordermanagementservice.dto.OrderUpdateDto;
import com.codedmdwsk.ordermanagementservice.repository.CustomerRepository;
import com.codedmdwsk.ordermanagementservice.repository.OrderRepository;

import jakarta.persistence.EntityManagerFactory;
import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.QueryCountHolder;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        QueryCountHolder.clear();
    }
    @Autowired
    EntityManagerFactory emf;

    @Test
    void listOrders_shouldNotTriggerNPlusOne_usingHibernateStats() throws Exception {
        SessionFactory sf = emf.unwrap(SessionFactory.class);
        Statistics stats = sf.getStatistics();
        stats.clear();

        for (int i = 0; i < 10; i++) {
            Customer c = createCustomer("C" + i);
            OrderData o = new OrderData();
            o.setCustomer(c);
            o.setDate(LocalDate.now());
            o.setTotalPrice(BigDecimal.ONE);
            o.setProducts("A");
            orderRepository.save(o);
        }

        OrderListRequestDto request = new OrderListRequestDto();
        request.setPage(0);
        request.setSize(20);
        stats.clear();


        mockMvc.perform(post("/api/orders/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list", hasSize(10)));


        long entityLoads = stats.getEntityLoadCount();
        System.out.println("ENTITY LOADS = " + entityLoads);
        long statements = stats.getPrepareStatementCount();
        System.out.println("STATEMENTS = " + statements);


        Assertions.assertTrue(statements <= 3,
                "Too many SQL statements: " + statements + " (possible N+1)");
    }

    @AfterEach
    void cleanup() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    private Customer createCustomer(String name) {
        Customer c = new Customer();
        c.setCustomerName(name);
        return customerRepository.save(c);
    }

    @Test
    void createOrder_shouldReturn201() throws Exception {
        var customer = createCustomer("Alex");

        OrderCreateDto dto = OrderCreateDto.builder()
                .customerId(customer.getId())
                .date(LocalDate.now())
                .totalPrice(BigDecimal.TEN)
                .products("A,B,C")
                .build();

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.products").value("A,B,C"));
    }

    @Test
    void getOrder_shouldReturnOrder() throws Exception {
        var customer = createCustomer("John");

        OrderData order = new OrderData();
        order.setCustomer(customer);
        order.setDate(LocalDate.now());
        order.setTotalPrice(BigDecimal.TEN);
        order.setProducts("X,Y,Z");

        order = orderRepository.save(order);

        mockMvc.perform(get("/api/orders/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.products").value("X,Y,Z"));
    }

    @Test
    void updateOrder_shouldReturnUpdatedObject() throws Exception {
        var customer1 = createCustomer("C1");
        var customer2 = createCustomer("C2");

        OrderData order = new OrderData();
        order.setCustomer(customer1);
        order.setDate(LocalDate.now());
        order.setTotalPrice(BigDecimal.TEN);
        order.setProducts("A");

        order = orderRepository.save(order);

        OrderUpdateDto dto = OrderUpdateDto.builder()
                .customerId(customer2.getId())
                .date(LocalDate.now().minusDays(1))
                .totalPrice(BigDecimal.valueOf(99))
                .products("Q,W,E")
                .build();

        mockMvc.perform(put("/api/orders/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(99));
    }

    @Test
    void deleteOrder_shouldReturn204() throws Exception {
        var customer = createCustomer("Alice");

        OrderData order = new OrderData();
        order.setCustomer(customer);
        order.setDate(LocalDate.now());
        order.setTotalPrice(BigDecimal.ONE);
        order.setProducts("A");
        orderRepository.save(order);

        mockMvc.perform(delete("/api/orders/" + order.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void listOrders_shouldReturnPagedData() throws Exception {
        var customer = createCustomer("A");

        OrderData o1 = new OrderData();
        o1.setCustomer(customer);
        o1.setDate(LocalDate.now());
        o1.setTotalPrice(BigDecimal.ONE);
        o1.setProducts("A,X");

        OrderData o2 = new OrderData();
        o2.setCustomer(customer);
        o2.setDate(LocalDate.now());
        o2.setTotalPrice(BigDecimal.ONE);
        o2.setProducts("B,Y");

        orderRepository.saveAll(List.of(o1, o2));

        OrderListRequestDto request = new OrderListRequestDto();
        request.setPage(0);
        request.setSize(10);

        mockMvc.perform(post("/api/orders/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list", hasSize(2)))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void uploadOrders_shouldReturnSuccessCounts() throws Exception {
        var customer = createCustomer("XX");

        String json = """
                [
                  {
                    "customerId": %d,
                    "date": "2024-01-01",
                    "totalPrice": 10,
                    "products": "A,B"
                  }
                ]
                """.formatted(customer.getId());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "orders.json",
                "application/json",
                json.getBytes()
        );

        mockMvc.perform(multipart("/api/orders/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successCount").value(1))
                .andExpect(jsonPath("$.failureCount").value(0));
    }

    @Test
    void reportCsv_shouldReturnCsvFile() throws Exception {
        var customer = createCustomer("Bob");

        OrderData order = new OrderData();
        order.setCustomer(customer);
        order.setDate(LocalDate.now());
        order.setTotalPrice(BigDecimal.ONE);
        order.setProducts("A,B");
        orderRepository.save(order);

        OrderListRequestDto request = new OrderListRequestDto();
        request.setCustomerId(customer.getId());
        request.setProducts("A");

        mockMvc.perform(
                        post("/api/orders/report")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/csv"));
    }

}
