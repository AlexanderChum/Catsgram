package ru.yandex.practicum.catsgram.model;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String errorMessage;
    private Object parameter;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(Object parameter, String errorMessage) {
        this.errorMessage = errorMessage;
        this.parameter = parameter;
    }
}
