package ru.mypackage.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.mypackage.dto.auth.RegistrationDTO;
import ru.mypackage.repository.UserRepository;

@Component
public class RegistrationDTOValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationDTOValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RegistrationDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegistrationDTO registrationDTO = (RegistrationDTO) target;

        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "This username is already taken!");
        }
    }
}
