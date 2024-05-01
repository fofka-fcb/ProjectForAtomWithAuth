package ru.mypackage.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.mypackage.dto.message.MessageDTO;
import ru.mypackage.dto.topic.TopicDTO;
import ru.mypackage.dto.user.UserDTO;
import ru.mypackage.exceptions.MessageNotFoundException;
import ru.mypackage.exceptions.TopicNotFoundException;
import ru.mypackage.exceptions.UserNotFoundException;
import ru.mypackage.services.MessageService;
import ru.mypackage.services.TopicService;
import ru.mypackage.services.UserService;
import ru.mypackage.utils.JWTFilter;
import ru.mypackage.utils.TopicDTOValidator;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private TopicService topicService;

    @MockBean
    private TopicDTOValidator topicDTOValidator;

    @MockBean
    private JWTFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldHaveCorrectGetAllUsers() throws Exception {
        when(userService.findAll())
                .thenReturn(List.of(new UserDTO("admin"), new UserDTO("user_1")));

        ResultActions response = mockMvc.perform(get("/admin/users"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userDTOList.size()").value(2));
    }

    @Test
    void shouldHaveCorrectGetOneUserById() throws Exception {
        Integer idOfUser = 1;
        when(userService.findOneById(idOfUser))
                .thenReturn(new UserDTO("admin"));

        ResultActions response = mockMvc.perform(get("/admin/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("admin"));
    }

    @Test
    void shouldHaveCorrectThrowExceptionWhenUserNotFound() throws Exception {
        Integer idOfUser = 3;
        when(userService.findOneById(idOfUser))
                .thenThrow(new UserNotFoundException("User not found!", HttpStatus.NOT_FOUND));

        ResultActions response = mockMvc.perform(get("/admin/3"));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found!"))
                .andDo(print());
    }

    @Test
    void shouldHaveCorrectRefTopicNameById() throws Exception {
        Integer idOfTopic = 1;
        TopicDTO topicDTO = new TopicDTO("updated name");
        doNothing().when(topicService).updateTopicName(idOfTopic, topicDTO);

        ResultActions response = mockMvc.perform(patch("/admin/topic/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topicDTO))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldHaveCorrectThrowExceptionWhenTopicNotFound() throws Exception {
        TopicDTO topicDTO = new TopicDTO("used name");

        doThrow(new TopicNotFoundException("Topic not found!", HttpStatus.NOT_FOUND))
                .when(topicService)
                .updateTopicName(any(Integer.class), any(TopicDTO.class));


        ResultActions response = mockMvc.perform(patch("/admin/topic/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topicDTO))
        );

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Topic not found!"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldHaveCorrectRefMessageById() throws Exception {
        MessageDTO messageDTO = new MessageDTO("some message");

        doNothing()
                .when(messageService)
                .updateMessage(any(Integer.class), any(MessageDTO.class), any(String.class));

        ResultActions response = mockMvc.perform(patch("/admin/message/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDTO))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldHaveCorrextThrowExceptionWhenRefMessageById() throws Exception {
        MessageDTO messageDTO = new MessageDTO("some message");

        doThrow(new MessageNotFoundException("Message with this id not found!", HttpStatus.NOT_FOUND))
                .when(messageService)
                .updateMessage(any(Integer.class), any(MessageDTO.class), any(String.class));

        ResultActions response = mockMvc.perform(patch("/admin/message/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDTO))
        );

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Message with this id not found!"));
    }

    @Test
    void shouldHaveCorrectDeleteTopicById() throws Exception {
        doNothing().when(topicService).deleteTopic(any(Integer.class));

        ResultActions response = mockMvc.perform(delete("/admin/topic/0"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldHaveCorrectThrowExceptionWhenDeleteTopicById() throws Exception {
        doThrow(new TopicNotFoundException("Topic not found!", HttpStatus.NOT_FOUND))
                .when(topicService)
                .deleteTopic(any(Integer.class));

        ResultActions response = mockMvc.perform(delete("/admin/topic/0"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Topic not found!"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldHaveCorrectDeleteMessageById() throws Exception {
        doNothing().when(messageService).deleteMessage(any(Integer.class), any(String.class));

        ResultActions response = mockMvc.perform(delete("/admin/message/0"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldHaveCorrectThrowExceptionWhenDeleteMessageById() throws Exception {
        doThrow(new MessageNotFoundException("Message with this id not found!", HttpStatus.NOT_FOUND))
                .when(messageService)
                .deleteMessage(any(Integer.class), any(String.class));

        ResultActions response = mockMvc.perform(delete("/admin/message/0"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Message with this id not found!"));
    }

}