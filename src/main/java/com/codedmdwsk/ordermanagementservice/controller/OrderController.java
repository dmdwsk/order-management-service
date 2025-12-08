package com.codedmdwsk.ordermanagementservice.controller;

import com.codedmdwsk.ordermanagementservice.dto.*;
import com.codedmdwsk.ordermanagementservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(@Valid @RequestBody OrderCreateDto dto) {
        return orderService.createOrder(dto);
    }
    @PostMapping("/list")
    public PagedResponse<OrderListDto> listOrders(@RequestBody OrderListRequestDto request){
        return orderService.listOrders(request);
    }


    @GetMapping("/{id}")
    public OrderResponseDto getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}")
    public OrderResponseDto updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderUpdateDto dto) {
        return orderService.updateOrder(id, dto);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }


}
