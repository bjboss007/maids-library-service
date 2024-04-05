package com.maids.librarysystem.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;


@Data
@Builder
public class AppResponse <T> {
    private boolean status;
    private int code;
    private String message;
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Meta meta;

    public static <T> ResponseEntity<AppResponse<T>> build(String message) {
        return build(message, null, null);
    }

    public static <T> ResponseEntity<AppResponse<T>> build(T data) {
        return build("Operation Successful!", data, null);
    }

    public static <T> ResponseEntity<AppResponse<T>> build(HttpStatus status, T data) {
        return build(status, "Operation Successful!", data, null);
    }

    public static <T> ResponseEntity<AppResponse<T>> build(T data, Meta meta) {
        return build("Operation Successful!", data, meta);
    }

    public static <D, T extends Page<D>> ResponseEntity<AppResponse<Collection<D>>> build(T data) {
        int totalPages = data.getTotalPages();
        int current = data.getNumber() + 1;
        long total = data.getTotalElements();
        int perPage = data.getSize();

        long from = ((long) (current - 1) * perPage) + 1 ;
        long to = Math.min(total, ((long) current * perPage));

        if (from > total) from = to = 0;

        AppResponse.Meta meta = Meta.builder()
                .currentPage(current)
                .from(from)
                .to(to)
                .perPage(perPage)
                .total(total)
                .lastPage(totalPages)
                .build();

        return build(data.getContent(), meta);
    }

    public static <T> ResponseEntity<AppResponse<T>> build(String message, T data, Meta meta) {
        return ResponseEntity.ok(AppResponse.<T>builder()
                .status(true)
                .code(200)
                .message(message)
                .data(data)
                .meta(meta)
                .build());
    }

    public static <T> ResponseEntity<AppResponse<T>> build(HttpStatus statusCode, String message, T data, Meta meta) {
        return ResponseEntity.ok(AppResponse.<T>builder()
                .status(true)
                .code(statusCode.value())
                .message(message)
                .data(data)
                .meta(meta)
                .build());
    }

    @Builder
    @Getter
    public static class Meta {
        private final long currentPage;
        private final long from;
        private final long to;
        private final long perPage;
        private final long total;
        private final long lastPage;

        public Meta(long currentPage, long from, long to, long perPage, long total, long lastPage) {
            this.currentPage = currentPage;
            this.from = from;
            this.to = to;
            this.perPage = perPage;
            this.total = total;
            this.lastPage = lastPage;
        }
    }

    @Override
    public String toString() {
        return "AppResponse{" +
                "status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", payload=" + data +
                ", meta=" + meta +
                '}';
    }
}
