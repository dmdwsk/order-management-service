package com.codedmdwsk.ordermanagementservice.service;

import com.codedmdwsk.ordermanagementservice.dto.OrderCreateDto;
import com.codedmdwsk.ordermanagementservice.dto.OrderResponseDto;
import com.codedmdwsk.ordermanagementservice.dto.OrderUpdateDto;

public interface OrderService {
    OrderResponseDto createOrder(OrderCreateDto dto);

    OrderResponseDto getOrderById(Long id);

    OrderResponseDto updateOrder(Long id, OrderUpdateDto dto);
    void deleteOrder(Long id);

}
