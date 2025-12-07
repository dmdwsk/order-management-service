package com.codedmdwsk.ordermanagementservice.service;

import com.codedmdwsk.ordermanagementservice.dto.OrderCreateDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class OrderServiceImpl {

    public static void validateStudents(OrderCreateDto dto){
        if(dto.getDate() != null && dto.getDate().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Date should be before now");
        }
        if (dto.getCustomerId() == null || dto.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Invalid customerId");
        }
        if (dto.getTotalPrice() == null) {
            throw new IllegalArgumentException("Total price cannot be null");
        }

        if (dto.getTotalPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
        if (dto.getProducts() == null || dto.getProducts().isBlank()) {
            throw new IllegalArgumentException("Products cannot be empty");
        }

        if (dto.getProducts().length() > 255) {
            throw new IllegalArgumentException("Products length cannot exceed 255 characters");
        }
        if (!dto.getProducts().matches("^[a-zA-Z0-9]+(,\\s*[a-zA-Z0-9]+)*$")) {
            throw new IllegalArgumentException("Products must be comma separated words");
        }

    }

}
