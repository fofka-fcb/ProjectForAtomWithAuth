package ru.mypackage.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageErrorResponse {
    private String message;
    private Long timestamp;
}
