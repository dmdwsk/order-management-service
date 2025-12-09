package com.codedmdwsk.ordermanagementservice.dto;

import com.codedmdwsk.ordermanagementservice.data.OrderData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class OrderResponseDto {
    private Long id;
    private LocalDate date;
    private BigDecimal totalPrice;
    private String products;
    private CustomerResponseDto customer;

    public static OrderResponseDto from(OrderData order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .date(order.getDate())
                .totalPrice(order.getTotalPrice())
                .products(order.getProducts())
                .customer(CustomerResponseDto.from(order.getCustomer()))
                .build();
    }
}