package com.codedmdwsk.ordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class OrderListRequestDto {
    private Long customerId;
    private String products;
    private Integer page;
    private Integer size;
}
