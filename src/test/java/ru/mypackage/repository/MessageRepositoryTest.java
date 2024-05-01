package ru.mypackage.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.mypackage.models.Message;
import ru.mypackage.models.Topic;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TopicRepository topicRepository;

    private Topic topic;
    private Message message_1;
    private Message message_2;
    private Message message_3;
    private List<Message> savedMessages;

    @BeforeEach
    void init() {
        topic = new Topic("First", Collections.emptyList());
        message_1 = new Message("user_1", "some message", new Date(), topic);
        message_2 = new Message("user_2", "some message", new Date(), topic);
        message_3 = new Message("user_3", "some message", new Date(), topic);
        savedMessages = new ArrayList<>(List.of(message_1, message_2, message_3));
        topic.setListOfMessages(savedMessages);

        topicRepository.save(topic);
        messageRepository.saveAll(savedMessages);
    }

    @Test
    void shouldHaveCorrectFindAllByTopic() {
        List<Message> messages = messageRepository.findAllByTopic(topic);

        assertThat(messages).isNotNull();
        assertThat(messages.size()).isEqualTo(3);
    }

    @Test
    void shouldHaveCorrectFindAllByTopicWithPagination() {
        //num of page
        int page = 0;
        //size of page
        int pageSize = 3;
        List<Message> messages = messageRepository.findAllByTopic(topic, PageRequest.of(
                page,
                pageSize,
                Sort.by("createdAt"))
        );

        assertThat(messages).isNotNull();
        assertThat(messages.size()).isEqualTo(3);
    }

    @Test
    void shouldHaveCorrectDeleteAllByTopic() {
        messageRepository.deleteAllByTopic(topic);
        List<Message> messages = messageRepository.findAllByTopic(topic);

        assertThat(messages).isEmpty();
    }

    @Test
    void shouldHaveCorrectDeleteMessageById() {
        messageRepository.delete(message_1.getId());
        List<Message> messages = messageRepository.findAllByTopic(topic);

        assertThat(messages.size()).isEqualTo(2);
    }
}
