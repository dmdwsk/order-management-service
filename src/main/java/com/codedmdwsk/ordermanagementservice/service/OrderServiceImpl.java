package com.codedmdwsk.ordermanagementservice.service;

import com.codedmdwsk.ordermanagementservice.data.Customer;
import com.codedmdwsk.ordermanagementservice.data.OrderData;
import com.codedmdwsk.ordermanagementservice.dto.*;
import com.codedmdwsk.ordermanagementservice.exceptions.NotFoundException;
import com.codedmdwsk.ordermanagementservice.repository.CustomerRepository;
import com.codedmdwsk.ordermanagementservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.codedmdwsk.ordermanagementservice.service.OrderSpecifications.byCustomerId;
import static com.codedmdwsk.ordermanagementservice.service.OrderSpecifications.byProduct;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    private void validateOrder(
            Long customerId,
            LocalDate date,
            BigDecimal totalPrice,
            String products
    ) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Invalid customerId");
        }
        if (date != null && date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
        if (totalPrice == null) {
            throw new IllegalArgumentException("Total price cannot be null");
        }
        if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
        if (products == null || products.isBlank()) {
            throw new IllegalArgumentException("Products cannot be empty");
        }
        if (products.length() > 255) {
            throw new IllegalArgumentException("Products length cannot exceed 255 characters");
        }
        if (!products.matches("^[a-zA-Z0-9]+(,\\s*[a-zA-Z0-9]+)*$")) {
            throw new IllegalArgumentException("Products must be comma-separated words");
        }
    }


    @Override
    public OrderResponseDto createOrder(OrderCreateDto dto) {
        validateOrder(
                dto.getCustomerId(),
                dto.getDate(),
                dto.getTotalPrice(),
                dto.getProducts()
        );
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        OrderData order = new OrderData();
        order.setCustomer(customer);
        order.setDate(dto.getDate());
        order.setTotalPrice(dto.getTotalPrice());
        order.setProducts(dto.getProducts());

        orderRepository.save(order);
        return OrderResponseDto.from(order);
    }

    @Override
    public OrderResponseDto getOrderById(Long id) {
        OrderData order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return OrderResponseDto.from(order);
    }

    @Override
    public OrderResponseDto updateOrder(Long id, OrderUpdateDto dto) {
        validateOrder(
                dto.getCustomerId(),
                dto.getDate(),
                dto.getTotalPrice(),
                dto.getProducts()
        );
        OrderData order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        order.setCustomer(customer);
        order.setDate(dto.getDate());
        order.setTotalPrice(dto.getTotalPrice());
        order.setProducts(dto.getProducts());

        orderRepository.save(order);
        return OrderResponseDto.from(order);
    }

    @Override
    public void deleteOrder(Long id) {
        OrderData order = orderRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Override
    public PagedResponse<OrderListDto> listOrders(OrderListRequestDto request) {

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize()
        );

        var spec = Specification.where(byCustomerId(request.getCustomerId()))
                .and(byProduct(request.getProducts()));

        var page = orderRepository.findAll(spec, pageable);


        return PagedResponse.of(
                page.getContent().stream()
                        .map(OrderListDto::from)
                        .toList(),
                page.getTotalPages()
        );
    }
}
