package com.guilhermepb.todosimple.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor    //required = final variables
@JsonInclude(JsonInclude.Include.NON_NULL)  //doesn't include null messages in the console
public class ErrorResponse {

    private final int status;
    private final String message;
    private String stackTrace;
    private List<ValidationError> errors;

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            this.errors = new ArrayList<>(); // if the object is null, this method passes the
        }                                    // "field" and the "message" for the error
        this.errors.add(new ValidationError(field, message));
    }

    public String toJson() {  //the message of error 402 (user write wrong the username or password)
        return "{\"status\": " + getStatus() + ", " +
                "\"message\": \"" + getMessage() + "\"}";
    }

}
