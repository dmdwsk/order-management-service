package com.codedmdwsk.ordermanagementservice.service;

import com.codedmdwsk.ordermanagementservice.data.Customer;
import com.codedmdwsk.ordermanagementservice.data.OrderData;
import com.codedmdwsk.ordermanagementservice.dto.CustomerCreateDto;
import com.codedmdwsk.ordermanagementservice.dto.CustomerResponseDto;
import com.codedmdwsk.ordermanagementservice.dto.CustomerUpdateDto;
import com.codedmdwsk.ordermanagementservice.exceptions.DuplicateCustomerException;
import com.codedmdwsk.ordermanagementservice.exceptions.NotFoundException;
import com.codedmdwsk.ordermanagementservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerResponseDto::from)
                .toList();
    }

    @Override
    public CustomerResponseDto create(CustomerCreateDto dto) {
        String name = dto.getCustomerName().trim();

        if (customerRepository.existsByCustomerNameIgnoreCase(name)) {
            throw new DuplicateCustomerException(
                    "Customer with name '" + name + "' already exists"
            );
        }
        Customer entity = new Customer();
        entity.setCustomerName(name);

        customerRepository.save(entity);

        return CustomerResponseDto.from(entity);
    }

    @Override
    public CustomerResponseDto update(Long id, CustomerUpdateDto dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        String newName = dto.getCustomerName().trim();
        boolean nameExists = customerRepository
                .existsByCustomerNameIgnoreCaseAndIdNot(newName, id);
        if (nameExists) {
            throw new DuplicateCustomerException(
                    "Customer with name '" + newName + "' already exists"
            );
        }

        customer.setCustomerName(newName);

        customerRepository.save(customer);

        return CustomerResponseDto.from(customer);

    }

    @Override
    public void deleteCustomer(Long id) {
        try {
            customerRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Cannot delete customer with existing orders");
        }
    }
}

