package ru.mypackage.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.mypackage.dto.message.MessageDTO;
import ru.mypackage.dto.topic.CreateTopicDTO;
import ru.mypackage.dto.topic.TopicDTO;
import ru.mypackage.models.Message;
import ru.mypackage.models.Topic;
import ru.mypackage.repository.MessageRepository;
import ru.mypackage.repository.TopicRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TopicService topicService;

    private Topic topic_1;
    private Topic topic_2;
    private Topic topic_3;
    private Topic topic_4;
    private Message message;
    private List<Topic> topics;
    private Page<Topic> topicPage;
    private CreateTopicDTO createTopicDTO;
    private TopicDTO topicDTO;
    private MessageDTO messageDTO;

    @BeforeEach
    void init() {
        topic_1 = new Topic();
        topic_2 = new Topic();
        topic_3 = new Topic();
        topic_4 = new Topic();

        message = new Message();

        topics = new ArrayList<>(List.of(topic_1, topic_2, topic_3, topic_4));
        topicPage = new PageImpl<>(List.of(topic_1, topic_2, topic_3));

        topicDTO = new TopicDTO();
        messageDTO = new MessageDTO();
        createTopicDTO = new CreateTopicDTO(topicDTO, messageDTO);
    }

    @Test
    void shouldHaveCorrectFindAllWhenPageNotNull() {
        //num of page
        int page = 0;

        when(topicRepository.findAll(any(PageRequest.class)))
                .thenReturn(topicPage);

        List<Topic> topicsFromService = topicService.findAll(page);

        assertThat(topicsFromService.size()).isEqualTo(3);
    }

    @Test
    void shouldHaveCorrectFindAllWhenPageIsNull() {
        when(topicRepository.findAll(any(Sort.class)))
                .thenReturn(topics);

        List<Topic> topicsFromService = topicService.findAll(null);

        assertThat(topicsFromService.size()).isEqualTo(4);
    }

    @Test
    void shouldHaveCorrectCreateTopicAndMessage() {
        String username = "user_1";

        when(modelMapper.map(createTopicDTO.getTopicDTO(), Topic.class)).thenReturn(topic_1);
        when(modelMapper.map(createTopicDTO.getMessageDTO(), Message.class)).thenReturn(message);
        when(topicRepository.save(topic_1)).thenReturn(topic_1);

        assertAll(() -> topicService.createTopicAndMessage(createTopicDTO, username));
    }

    @Test
    void shouldHaveCorrectUpdateTopicName() {
        int idOfTopic = 1;

        when(topicRepository.findById(idOfTopic)).thenReturn(Optional.of(topic_1));
        when(topicRepository.save(topic_1)).thenReturn(topic_1);

        assertAll(() -> topicService.updateTopicName(idOfTopic, topicDTO));
    }

    @Test
    void shouldHaveCorrectDeleteTopic() {
        int idOfTopic = 1;

        when(topicRepository.findById(idOfTopic)).thenReturn(Optional.of(topic_1));
        doNothing().when(messageRepository).deleteAllByTopic(topic_1);
        doNothing().when(topicRepository).delete(topic_1);

        assertAll(() -> topicService.deleteTopic(idOfTopic));
    }

}
