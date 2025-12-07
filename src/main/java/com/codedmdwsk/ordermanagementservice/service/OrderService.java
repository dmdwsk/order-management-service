package com.codedmdwsk.ordermanagementservice.service;

import com.codedmdwsk.ordermanagementservice.dto.OrderCreateDto;

public interface OrderService {
    int createOrder(OrderCreateDto dto);
}
