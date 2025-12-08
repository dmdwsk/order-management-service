package com.codedmdwsk.ordermanagementservice.service;

import com.codedmdwsk.ordermanagementservice.data.Customer;
import com.codedmdwsk.ordermanagementservice.data.OrderData;
import com.codedmdwsk.ordermanagementservice.dto.*;
import com.codedmdwsk.ordermanagementservice.exceptions.NotFoundException;
import com.codedmdwsk.ordermanagementservice.repository.CustomerRepository;
import com.codedmdwsk.ordermanagementservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.codedmdwsk.ordermanagementservice.service.OrderSpecifications.byCustomerId;
import static com.codedmdwsk.ordermanagementservice.service.OrderSpecifications.byProduct;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

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

    @Override
    public void writeReportToCsv(OrderListRequestDto request, Writer writer) {
        var spec = Specification.where(byCustomerId(request.getCustomerId())).and(byProduct(request.getProducts()));
        List<OrderData> all = orderRepository.findAll(spec);
        try(writer){
            writer.write("ID;Date;TotalPrice;Products;CustomerName\n");
            for (OrderData o : all) {
                writer.write(
                        o.getId() + ";" +
                                o.getDate() + ";" +
                                o.getTotalPrice() + ";" +
                                o.getProducts() + ";" +
                                o.getCustomer().getCustomerName() +
                                "\n"
                );
            }
        }catch (IOException e){
            throw new RuntimeException("CSV writing error", e);
        }
    }

    @Override
    public UploadResponseDto upload(MultipartFile file) {
        int success = 0;
        int failure = 0;
        try {
            List<OrderCreateDto> items = objectMapper.readValue(
                    file.getBytes(),
                    new TypeReference<List<OrderCreateDto>>() {}
            );
            for(OrderCreateDto dto: items){
                try{
                    validateOrder(
                            dto.getCustomerId(),
                            dto.getDate(),
                            dto.getTotalPrice(),
                            dto.getProducts()
                    );
                    Customer customer = customerRepository.findById(dto.getCustomerId())
                            .orElseThrow(() -> new NotFoundException("Customer not found"));
                    OrderData entity = new OrderData();
                    entity.setCustomer(customer);
                    entity.setDate(dto.getDate());
                    entity.setTotalPrice(dto.getTotalPrice());
                    entity.setProducts(dto.getProducts());

                    orderRepository.save(entity);
                    success++;
                }catch (Exception ex){
                    failure++;
                }
            }
        }catch (IOException e){
            throw new RuntimeException("Invalid JSON file format");
        }
        return UploadResponseDto.builder()
                .successCount(success)
                .failureCount(failure)
                .build();
    }

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.
                findAll().stream().
                map(CustomerResponseDto::from)
                .toList();
    }
}
