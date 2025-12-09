package com.codedmdwsk.ordermanagementservice.controller;

import com.codedmdwsk.ordermanagementservice.data.Customer;
import com.codedmdwsk.ordermanagementservice.dto.CustomerCreateDto;
import com.codedmdwsk.ordermanagementservice.dto.CustomerUpdateDto;
import com.codedmdwsk.ordermanagementservice.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@ActiveProfiles("test")
class CustomerControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void clear() {
        customerRepository.deleteAll();
    }

    @Test
    void createCustomer_shouldReturnCreated() throws Exception {
        CustomerCreateDto dto = new CustomerCreateDto();
        dto.setCustomerName("John");

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.customerName").value("John"));
    }

    @Test
    void createCustomer_duplicateName_shouldReturn409() throws Exception {
        Customer c = new Customer();
        c.setCustomerName("John");
        customerRepository.save(c);

        CustomerCreateDto dto = new CustomerCreateDto();
        dto.setCustomerName("John");

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());

    }

    @Test
    void getAllCustomers_shouldReturnList() throws Exception {
        Customer c1 = new Customer();
        c1.setCustomerName("A");
        customerRepository.save(c1);

        Customer c2 = new Customer();
        c2.setCustomerName("B");
        customerRepository.save(c2);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void updateCustomer_shouldUpdateFields() throws Exception {
        Customer saved = new Customer();
        saved.setCustomerName("Old");
        saved = customerRepository.save(saved);

        CustomerUpdateDto dto = new CustomerUpdateDto();
        dto.setCustomerName("NewName");

        mockMvc.perform(put("/api/customers/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("NewName"));
    }

    @Test
    void deleteCustomer_shouldReturn204() throws Exception {
        Customer saved = new Customer();
        saved.setCustomerName("Test");
        saved = customerRepository.save(saved);

        mockMvc.perform(delete("/api/customers/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

}
