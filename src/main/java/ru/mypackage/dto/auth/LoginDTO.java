package ru.mypackage.dto.auth;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String username;

    @Size(min = 4, max = 12, message = "Password should be between 4 and 12 characters")
    private String password;

}
