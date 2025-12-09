package com.codedmdwsk.ordermanagementservice.dto;

import com.codedmdwsk.ordermanagementservice.data.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {
    private Long id;
    private String customerName;
    public static CustomerResponseDto from(Customer entity) {
        return new CustomerResponseDto(
                entity.getId(),
                entity.getCustomerName()
        );
    }
}
