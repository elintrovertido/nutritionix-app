package com.nutritionix.authentication_service.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestError {
    private String field;
    private String message;
}
