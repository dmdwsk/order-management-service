package com.codedmdwsk.ordermanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@Jacksonized
public class OrderUpdateDto  {
    @NotNull
    @Positive
    private Long customerId;

    @NotNull
    private LocalDate date;

    @NotNull
    private BigDecimal totalPrice;

    @NotBlank
    private String products;
}
