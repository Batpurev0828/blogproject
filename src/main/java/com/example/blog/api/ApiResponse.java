package com.example.blog.api;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;
    public ApiResponse(int status, String message, T response) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.data = response;
    }

    public T getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
