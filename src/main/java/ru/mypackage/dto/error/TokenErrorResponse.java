package ru.mypackage.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenErrorResponse {
    private String message;
    private long timestamp;
}
