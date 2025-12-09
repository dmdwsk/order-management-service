package com.codedmdwsk.ordermanagementservice.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class OrderListRequestDto {
    private Long customerId;
    private String products;
    private Integer page;
    private Integer size;
}
