package com.codedmdwsk.ordermanagementservice.dto;

import com.codedmdwsk.ordermanagementservice.data.Customer;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerResponseDto {
    private Long id;
    private String customerName;
    public static CustomerResponseDto from(Customer customer){
        return CustomerResponseDto.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .build();
    }
}
