package com.codedmdwsk.ordermanagementservice.service;

import com.codedmdwsk.ordermanagementservice.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Writer;
import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderCreateDto dto);

    OrderResponseDto getOrderById(Long id);

    OrderResponseDto updateOrder(Long id, OrderUpdateDto dto);
    void deleteOrder(Long id);
    PagedResponse<OrderListDto> listOrders(OrderListRequestDto request);
    void writeReportToCsv(OrderListRequestDto request, Writer writer);
    UploadResponseDto upload(MultipartFile file);
    List<CustomerResponseDto> getAllCustomers();

}
