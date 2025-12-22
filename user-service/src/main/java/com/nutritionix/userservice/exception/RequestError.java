package com.nutritionix.userservice.exception;

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
