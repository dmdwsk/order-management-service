package com.codedmdwsk.ordermanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PagedResponse<T> {
    private List<T> list;
    private int totalPages;

    public static <T> PagedResponse<T> of(List<T> list, int totalPages) {
        return PagedResponse.<T>builder()
                .list(list)
                .totalPages(totalPages)
                .build();
    }
}
