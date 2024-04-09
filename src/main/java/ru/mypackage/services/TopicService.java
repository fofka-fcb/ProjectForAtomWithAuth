package ru.mypackage.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mypackage.dto.message.MessageDTO;
import ru.mypackage.dto.topic.CreateTopicDTO;
import ru.mypackage.dto.topic.TopicDTO;
import ru.mypackage.exceptions.TopicNotFoundException;
import ru.mypackage.models.Message;
import ru.mypackage.models.Topic;
import ru.mypackage.repository.MessageRepository;
import ru.mypackage.repository.TopicRepository;

import java.util.Date;
import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public TopicService(TopicRepository topicRepository,
                        MessageRepository messageRepository,
                        ModelMapper modelMapper
    ) {
        this.topicRepository = topicRepository;
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<Topic> findAll(Integer page) {
        if (page != null)
            return topicRepository.findAll(
                    PageRequest.of(
                            page,
                            1,
                            Sort.by("createdAt"))
            ).toList();

        else return topicRepository.findAll(Sort.by("createdAt"));
    }

    @Transactional
    public void createTopicAndMessage(CreateTopicDTO createTopicDTO, String username) {
        Topic createdTopic = convertToTopic(createTopicDTO.getTopicDTO());
        Message createdMessage = convertToMessage(createTopicDTO.getMessageDTO());

        createdMessage.setTopic(createdTopic);
        createdMessage.setUsername(username);
        createdMessage.setCreatedAt(new Date());

        createdTopic.setCreatedAt(new Date());
        createdTopic.setListOfMessages(List.of(createdMessage));

        topicRepository.save(createdTopic);
    }

    @Transactional
    public void updateTopicName(Integer idOfTopic, TopicDTO topicDTO) {
        Topic topicFropmRepo = findTopicFromRepo(idOfTopic);

        topicFropmRepo.setName(topicDTO.getName());

        topicRepository.save(topicFropmRepo);
    }

    @Transactional
    public void deleteTopic(Integer idOfTopic) {
        Topic topicFromRepo = findTopicFromRepo(idOfTopic);

        messageRepository.deleteAllByTopic(topicFromRepo);
        topicRepository.delete(topicFromRepo);
    }

    protected Topic findTopicFromRepo(Integer idOfTopic) {
        return topicRepository.findById(idOfTopic)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found!", HttpStatus.NOT_FOUND));
    }

    private Topic convertToTopic(TopicDTO topicDTO) {
        return modelMapper.map(topicDTO, Topic.class);
    }

    private Message convertToMessage(MessageDTO messageDTO) {
        return modelMapper.map(messageDTO, Message.class);
    }
}
