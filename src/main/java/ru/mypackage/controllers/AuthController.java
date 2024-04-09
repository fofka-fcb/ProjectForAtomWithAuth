package ru.mypackage.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.dto.auth.LoginDTO;
import ru.mypackage.dto.auth.LoginResponseDTO;
import ru.mypackage.dto.auth.RegisterResponseDTO;
import ru.mypackage.dto.auth.RegistrationDTO;
import ru.mypackage.exceptions.AuthValidationException;
import ru.mypackage.services.auth.AuthenticationService;
import ru.mypackage.utils.RegistrationDTOValidator;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final RegistrationDTOValidator registrationDTOValidator;

    @Autowired
    public AuthController(AuthenticationService authenticationService,
                          RegistrationDTOValidator registrationDTOValidator
    ) {
        this.authenticationService = authenticationService;
        this.registrationDTOValidator = registrationDTOValidator;
    }

    @PostMapping("/register")
    public RegisterResponseDTO registerUser(
            @RequestBody @Valid RegistrationDTO body,
            BindingResult bindingResult
    ) {
        registrationDTOValidator.validate(body, bindingResult);

        if (bindingResult.hasErrors()) throw new AuthValidationException(bindingResult);

        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(
            @RequestBody @Valid LoginDTO body,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) throw new AuthValidationException(bindingResult);

        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }

}
