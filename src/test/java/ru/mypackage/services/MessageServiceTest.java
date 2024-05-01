package ru.mypackage.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import ru.mypackage.dto.message.MessageDTO;
import ru.mypackage.models.Message;
import ru.mypackage.models.Topic;
import ru.mypackage.repository.MessageRepository;
import ru.mypackage.repository.TopicRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MessageService messageService;

    private int idOfTopic;
    private int idOfMessage;
    private String username;
    private Topic topic;
    private MessageDTO messageDTO;
    private Message message_1;
    private Message message_2;
    private Message message_3;
    private List<Message> messages;

    @BeforeEach
    void init() {
        idOfTopic = 1;
        idOfMessage = 1;
        username = "user_1";

        topic = new Topic(1, "first", new Date(), Collections.emptyList());
        messageDTO = new MessageDTO("some message");
        message_1 = new Message("user_1", "some message", new Date(), topic);
        message_2 = new Message("user_2", "some message", new Date(), topic);
        message_3 = new Message("user_3", "some message", new Date(), topic);
        messages = new ArrayList<>(List.of(message_1, message_2, message_3));
        topic.setListOfMessages(messages);
    }

    @Test
    void shouldHaveCorrectFindAllByIdOfTopicWithPagination() {
        //num of page
        int page = 0;

        when(topicRepository.findById(any(Integer.class))).thenReturn(Optional.of(topic));
        when(messageRepository.findAllByTopic(any(Topic.class), any(PageRequest.class)))
                .thenReturn(messages);

        List<Message> messagesFromMessageService =
                messageService.findAllByIdOfTopic(idOfTopic, page);

        assertThat(messagesFromMessageService.size()).isEqualTo(3);
    }

    @Test
    void shouldHaveCorrectFindAllByIdOfTopicWithoutPagination() {
        when(topicRepository.findById(any(Integer.class))).thenReturn(Optional.of(topic));
        when(messageRepository.findAllByTopic(any(Topic.class)))
                .thenReturn(messages);

        List<Message> messagesFromMessageService =
                messageService.findAllByIdOfTopic(idOfTopic, null);

        assertThat(messagesFromMessageService.size()).isEqualTo(3);
    }

    @Test
    void shouldHaveCorrectCreateMessage() {
        when(topicRepository.findById(idOfTopic)).thenReturn(Optional.of(topic));
        when(modelMapper.map(messageDTO, Message.class)).thenReturn(message_1);
        when(messageRepository.save(message_1)).thenReturn(message_1);

        assertAll(() -> messageService.createMessage(idOfTopic, messageDTO, username));
    }

    @Test
    void shouldHaveCorrectUpdateMessage() {
        when(messageRepository.findById(idOfMessage)).thenReturn(Optional.of(message_1));
        when(messageRepository.save(message_1)).thenReturn(message_1);

        assertAll(() -> messageService.updateMessage(idOfMessage, messageDTO, username));
    }

    @Test
    void shouldHaveCorrectDeleteMessageWhenTopicNotEmpty() {
        when(messageRepository.findById(idOfMessage)).thenReturn(Optional.of(message_1));
        when(topicRepository.findById(idOfTopic)).thenReturn(Optional.of(topic));
        doNothing().when(messageRepository).delete(idOfMessage);

        assertAll(() -> messageService.deleteMessage(idOfMessage, username));
    }
}
