package ru.mypackage.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import ru.mypackage.dto.topic.CreateTopicDTO;
import ru.mypackage.exceptions.MessageNotFoundException;
import ru.mypackage.exceptions.TopicNotFoundException;
import ru.mypackage.models.Message;
import ru.mypackage.models.Topic;
import ru.mypackage.services.MessageService;
import ru.mypackage.services.TopicService;
import ru.mypackage.utils.JWTFilter;
import ru.mypackage.utils.TopicDTOValidator;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TopicService topicService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private TopicDTOValidator topicDTOValidator;

    @MockBean
    private JWTFilter jwtFilter;

    private Topic topic;
    private Message message_1;
    private Message message_2;
    private Message message_3;
    private Message message_4;
    private MessageDTO messageDTO;

    @BeforeEach
    void init() {
        topic = new Topic(1, "first", new Date(), Collections.emptyList());

        message_1 = new Message(1, "user_1", "some message", new Date(), topic);
        message_2 = new Message(2, "user_1", "some message", new Date(), topic);
        message_3 = new Message(3, "user_1", "some message", new Date(), topic);
        message_4 = new Message(4, "user_1", "some message", new Date(), topic);

        topic.setListOfMessages(List.of(message_1, message_2, message_3, message_4));

        messageDTO = new MessageDTO("some message");
    }

    @Test
    void shouldHaveCorrectGetAllTopicsWhenPageNotNull() throws Exception {
        when(topicService.findAll(any(Integer.class)))
                .thenReturn(List.of(new Topic(), new Topic(), new Topic()));

        ResultActions response = mockMvc.perform(get("/topic/all?page=0"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.topics.size()").value(3));
    }

    @Test
    void shouldHaveCorrectGetAllTopicsWhenPageIsNull() throws Exception {
        when(topicService.findAll(null))
                .thenReturn(List.of(new Topic(), new Topic(), new Topic(), new Topic()));

        ResultActions response = mockMvc.perform(get("/topic/all"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.topics.size()").value(4));
    }

    @Test
    void shouldHaveCorrectGetAllMessagesFromTopicWhenPageNotNull() throws Exception {
        when(messageService.findAllByIdOfTopic(any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(message_1, message_2, message_3));

        ResultActions response = mockMvc.perform(get("/topic/1?page=0"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageList.size()").value(3));
    }

    @Test
    void shouldHaveCorrectGetAllMessagesFromTopicWhenPageIsNull() throws Exception {
        when(messageService.findAllByIdOfTopic(topic.getId(), null))
                .thenReturn(topic.getListOfMessages());

        ResultActions response = mockMvc.perform(get("/topic/1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageList.size()").value(4));
    }

    @Test
    void shouldHaveCorrectThrowExceptionWhenGetAllMessages() throws Exception {
        when(messageService.findAllByIdOfTopic(any(Integer.class), any(Integer.class)))
                .thenThrow(new TopicNotFoundException("Topic not found!", HttpStatus.NOT_FOUND));

        ResultActions response = mockMvc.perform(get("/topic/1?page=0"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Topic not found!"));
    }

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectCreateTopicWithMessage() throws Exception {
        doNothing()
                .when(topicService)
                .createTopicAndMessage(any(CreateTopicDTO.class), any(String.class));

        ResultActions response = mockMvc.perform(post("/topic/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateTopicDTO())
                )
        );

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectCreateNewMessage() throws Exception {
        doNothing()
                .when(messageService)
                .createMessage(any(Integer.class), any(MessageDTO.class), any(String.class));

        ResultActions response = mockMvc.perform(post("/topic/create/message/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDTO))
        );

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectTrowExceptionWhenCreateNewMessage() throws Exception {
        doThrow(new TopicNotFoundException("Topic not found!", HttpStatus.NOT_FOUND))
                .when(messageService)
                .createMessage(any(Integer.class), any(MessageDTO.class), any(String.class));

        ResultActions response = mockMvc.perform(post("/topic/create/message/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDTO))
        );

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Topic not found!"));
    }

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectRefMessage() throws Exception {
        doNothing()
                .when(messageService)
                .updateMessage(any(Integer.class), any(MessageDTO.class), any(String.class));

        ResultActions response = mockMvc.perform(patch("/topic/message/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDTO))
        );

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectThrowExceptionWhenRefMessage() throws Exception {
        doThrow(new MessageNotFoundException("Message with this id not found!", HttpStatus.NOT_FOUND))
                .when(messageService)
                .updateMessage(any(Integer.class), any(MessageDTO.class), any(String.class));

        ResultActions response = mockMvc.perform(patch("/topic/message/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDTO))
        );

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Message with this id not found!"));
    }

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectDeleteMessage() throws Exception {
        doNothing()
                .when(messageService)
                .deleteMessage(any(Integer.class), any(String.class));

        ResultActions response = mockMvc.perform(delete("/topic/message/1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectThrowExceptionWhenDeleteMessage() throws Exception {
        doThrow(new MessageNotFoundException("Message with this id not found!", HttpStatus.NOT_FOUND))
                .when(messageService)
                .deleteMessage(any(Integer.class), any(String.class));

        ResultActions response = mockMvc.perform(delete("/topic/message/1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Message with this id not found!"));
    }
}
