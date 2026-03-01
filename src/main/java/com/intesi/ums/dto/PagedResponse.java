package com.intesi.ums.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Paginated response wrapper")
@Builder
public record PagedResponse<T>(

    @Schema(description = "Page content")
    List<T> content,

    @Schema(description = "Current page number (0-indexed)", example = "0")
    int page,

    @Schema(description = "Page size", example = "20")
    int size,

    @Schema(description = "Total number of elements")
    long totalElements,

    @Schema(description = "Total number of pages")
    int totalPages,

    @Schema(description = "Whether this is the last page")
    boolean last

) {
    public static <T> PagedResponse<T> from(Page<T> page) {
        return PagedResponse.<T>builder()
            .content(page.getContent())
            .page(page.getNumber())
            .size(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .last(page.isLast())
            .build();
    }
}
