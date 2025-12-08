package com.codedmdwsk.ordermanagementservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadResponseDto {
    private int successCount;
    private int failureCount;
}
