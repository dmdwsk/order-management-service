package com.codedmdwsk.ordermanagementservice.service;

import com.codedmdwsk.ordermanagementservice.dto.*;

public interface OrderService {
    OrderResponseDto createOrder(OrderCreateDto dto);

    OrderResponseDto getOrderById(Long id);

    OrderResponseDto updateOrder(Long id, OrderUpdateDto dto);
    void deleteOrder(Long id);
    PagedResponse<OrderListDto> listOrders(OrderListRequestDto request);

}
