package com.codedmdwsk.ordermanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdateDto {
    @NotBlank
    private String customerName;
}
