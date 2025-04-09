package com.example.social.media.payload.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private int currentPage;
    private int totalPage;
    private int pageSize;
    private Long totalElements;
    private List<T> data = Collections.emptyList();

    public PageResponse(Page<T> page) {
        this.currentPage = page.getNumber();
        this.totalPage = page.getTotalPages();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.data = page.getContent();
    }
}

