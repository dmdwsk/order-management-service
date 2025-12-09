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
public class OrderListDto {
    private Long id;
    private LocalDate date;
    private BigDecimal totalPrice;
    private String customerName;

    public static OrderListDto from(OrderData order){
        return OrderListDto.builder()
                .id(order.getId())
                .date(order.getDate())
                .totalPrice(order.getTotalPrice())
                .customerName(order.getCustomer().getCustomerName())
                .build();
    }
}
