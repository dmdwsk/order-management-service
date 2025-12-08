package com.codedmdwsk.ordermanagementservice.controller;

import com.codedmdwsk.ordermanagementservice.dto.CustomerCreateDto;
import com.codedmdwsk.ordermanagementservice.dto.CustomerResponseDto;
import com.codedmdwsk.ordermanagementservice.dto.CustomerUpdateDto;
import com.codedmdwsk.ordermanagementservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<CustomerResponseDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDto create(@Valid @RequestBody CustomerCreateDto dto) {
        return customerService.create(dto);
    }
    @PutMapping("/{id}")
    public CustomerResponseDto updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateDto dto
    ) {
        return customerService.update(id, dto);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
}
