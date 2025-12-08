package com.codedmdwsk.ordermanagementservice.service;

import com.codedmdwsk.ordermanagementservice.dto.CustomerCreateDto;
import com.codedmdwsk.ordermanagementservice.dto.CustomerResponseDto;
import com.codedmdwsk.ordermanagementservice.dto.CustomerUpdateDto;

import java.util.List;

public interface CustomerService {
    List<CustomerResponseDto> getAllCustomers();
    CustomerResponseDto create(CustomerCreateDto dto);
    CustomerResponseDto update(Long id, CustomerUpdateDto dto);
    void deleteCustomer(Long id);

}
