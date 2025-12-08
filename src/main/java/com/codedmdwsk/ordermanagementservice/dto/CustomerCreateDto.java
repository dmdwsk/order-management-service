package com.codedmdwsk.ordermanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerCreateDto {
    @NotBlank(message = "Customer name cannot be empty")
    @Size(max = 50, message = "Customer name cannot exceed 50 characters")
    private String customerName;
}
