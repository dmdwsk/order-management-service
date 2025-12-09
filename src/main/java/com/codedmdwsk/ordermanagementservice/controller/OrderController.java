package com.codedmdwsk.ordermanagementservice.controller;

import com.codedmdwsk.ordermanagementservice.dto.*;
import com.codedmdwsk.ordermanagementservice.service.CustomerService;
import com.codedmdwsk.ordermanagementservice.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

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
    @PostMapping("/_list")
    public PagedResponse<OrderListDto> listOrders(@RequestBody OrderListRequestDto request){
        return orderService.listOrders(request);
    }
    @PostMapping("/report")
    public void downloadReport(
            @RequestBody OrderListRequestDto request,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"orders-report.csv\""
        );
        orderService.writeReportToCsv(request,response.getWriter());

    }
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public UploadResponseDto upload(@RequestParam("file") MultipartFile file) {
        return orderService.upload(file);
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
