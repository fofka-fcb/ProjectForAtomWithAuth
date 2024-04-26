package ru.mypackage.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.mypackage.dto.auth.LoginDTO;
import ru.mypackage.dto.auth.LoginResponseDTO;
import ru.mypackage.dto.auth.RegisterResponseDTO;
import ru.mypackage.dto.auth.RegistrationDTO;
import ru.mypackage.models.Role;
import ru.mypackage.services.auth.AuthenticationService;
import ru.mypackage.utils.JWTFilter;
import ru.mypackage.utils.RegistrationDTOValidator;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Set;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private RegistrationDTOValidator registrationDTOValidator;

    @MockBean
    private JWTFilter jwtFilter;

    @Test
    void shouldCorrectRegUser() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("user_1", "password");

        RegisterResponseDTO registerResponseDTO =
                new RegisterResponseDTO("user_1", "password", Set.of(new Role("USER")));

        when(authenticationService.registerUser(registrationDTO.getUsername(), registrationDTO.getPassword()))
                .thenReturn(registerResponseDTO);

        ResultActions responser = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO))
        );

        responser.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user_1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("password"));
    }

    @Test
    void shouldCorrectLoginUser() throws Exception {
        LoginDTO loginDTO = new LoginDTO("user_1", "password");

        LoginResponseDTO loginResponseDTO =
                new LoginResponseDTO("user_1", Set.of(new Role("User")), "qwe123");

        when(authenticationService.loginUser(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(loginResponseDTO);

        ResultActions response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO))
        );

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user_1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt").value("qwe123"));
    }
}
