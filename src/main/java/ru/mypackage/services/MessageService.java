package ru.mypackage.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mypackage.dto.message.MessageDTO;
import ru.mypackage.exceptions.ForbiddenException;
import ru.mypackage.exceptions.MessageNotFoundException;
import ru.mypackage.exceptions.TopicNotFoundException;
import ru.mypackage.models.Message;
import ru.mypackage.models.Topic;
import ru.mypackage.repository.MessageRepository;
import ru.mypackage.repository.TopicRepository;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          TopicRepository topicRepository,
                          ModelMapper modelMapper
    ) {
        this.messageRepository = messageRepository;
        this.topicRepository = topicRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<Message> findAllByIdOfTopic(Integer idOfTopic, Integer page) {
        Topic topicFromRepo = findTopicFromRepo(idOfTopic);

        if (page != null)
            return messageRepository.findAllByTopic(topicFromRepo, PageRequest.of(
                    page,
                    3,
                    Sort.by("createdAt"))
            );

        else return messageRepository.findAllByTopic(topicFromRepo);
    }

    @Transactional
    public void createMessage(Integer idOfTopic, MessageDTO messageDTO, String username) {
        Topic topicFromRepo = findTopicFromRepo(idOfTopic);

        Message createdMessage = convertToMessage(messageDTO);
        createdMessage.setTopic(topicFromRepo);
        createdMessage.setUsername(username);
        createdMessage.setCreatedAt(new Date());

        messageRepository.save(createdMessage);
    }

    @Transactional
    public void updateMessage(Integer idOfMessage, MessageDTO MessageDTO, String username) {
        Message messageFromRepo = findMessageFromRepo(idOfMessage);

        if (messageFromRepo.getUsername().equals(username) || username.equals("admin")) {
            messageFromRepo.setMessage(MessageDTO.getMessage());
            messageRepository.save(messageFromRepo);
        } else {
            throw new ForbiddenException("Can't update!", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void deleteMessage(Integer idOfDeletedMessage, String username) {
        Message messageFromRepo = findMessageFromRepo(idOfDeletedMessage);

        if (messageFromRepo.getUsername().equals(username) || username.equals("admin")) {
            messageRepository.delete(idOfDeletedMessage);

            Topic topicFromRepo = findTopicFromRepo(messageFromRepo.getTopicId());

            if (topicFromRepo.getListOfMessages().isEmpty()) {
                topicRepository.delete(topicFromRepo);
            }
        } else {
            throw new ForbiddenException("Can't delete!", HttpStatus.BAD_REQUEST);
        }
    }

    protected Message findMessageFromRepo(Integer idOfMessage) {
        return messageRepository.findById(idOfMessage)
                .orElseThrow(() -> new MessageNotFoundException("Message with this id not found!", HttpStatus.NOT_FOUND));
    }

    protected Topic findTopicFromRepo(Integer idOfTopic) {
        return topicRepository.findById(idOfTopic)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found!", HttpStatus.NOT_FOUND));
    }

    private Message convertToMessage(MessageDTO messageDTO) {
        return modelMapper.map(messageDTO, Message.class);
    }

}
