package ru.mypackage.dto.message;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    @Size(min = 6, max = 256, message = "Message should be between 6 and 256 characters")
    private String message;

}
