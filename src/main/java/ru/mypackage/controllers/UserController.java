package ru.mypackage.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.dto.message.MessageDTO;
import ru.mypackage.dto.message.MessagesResponseDTO;
import ru.mypackage.dto.topic.CreateTopicDTO;
import ru.mypackage.dto.topic.TopicsResponseDTO;
import ru.mypackage.exceptions.MessageDTOValidationException;
import ru.mypackage.exceptions.TopicDTOValidationException;
import ru.mypackage.services.MessageService;
import ru.mypackage.services.TopicService;
import ru.mypackage.utils.TopicDTOValidator;

@RestController
@CrossOrigin("*")
@RequestMapping("/topic")
public class UserController {

    private final TopicService topicService;
    private final MessageService messageService;
    private final TopicDTOValidator topicDTOValidator;

    @Autowired
    public UserController(TopicService topicService,
                          MessageService messageService,
                          TopicDTOValidator topicDTOValidator
    ) {
        this.topicService = topicService;
        this.messageService = messageService;
        this.topicDTOValidator = topicDTOValidator;
    }

    //Получение списка всех топиков и сообщений
    @GetMapping("/all")
    public ResponseEntity<TopicsResponseDTO> getAllTopics(
            @RequestParam(value = "page", required = false) Integer page
    ) {
        TopicsResponseDTO topicResponseDTO = new TopicsResponseDTO(
                topicService.findAll(page)
        );

        return new ResponseEntity<>(topicResponseDTO, HttpStatus.OK);
    }

    //Получение списка сообщений одного топика
    @GetMapping("/{id}")
    public ResponseEntity<MessagesResponseDTO> getAllMessageFromTopic(
            @PathVariable("id") Integer id,
            @RequestParam(value = "page", required = false) Integer page
    ) {
        MessagesResponseDTO messageResponseDTO = new MessagesResponseDTO(
                messageService.findAllByIdOfTopic(id, page)
        );

        return new ResponseEntity<>(messageResponseDTO, HttpStatus.OK);
    }

    //Создание нового топика с одним сообщением
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createTopicWithMessage(
            @RequestBody @Valid CreateTopicDTO createTopicDTO,
            BindingResult bindingResult
    ) {
        topicDTOValidator.validate(createTopicDTO.getTopicDTO(), bindingResult);

        if (bindingResult.hasErrors())
            throw new TopicDTOValidationException(bindingResult);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        topicService.createTopicAndMessage(createTopicDTO, username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    //Создание нового сообщения
    @PostMapping("/create/message/{id}")
    public ResponseEntity<HttpStatus> createMessage(
            //Id топика
            @PathVariable Integer id,
            @RequestBody @Valid MessageDTO messageDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors())
            throw new MessageDTOValidationException(bindingResult);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        messageService.createMessage(id, messageDTO, username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    //Редактирование сообщения
    @PatchMapping("/message/{id}")
    public ResponseEntity<HttpStatus> refactorMessage(
            //Id изменяемого сообщения
            @PathVariable Integer id,
            @RequestBody @Valid MessageDTO messageDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors())
            throw new MessageDTOValidationException(bindingResult);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        messageService.updateMessage(id, messageDTO, username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    //Удаление сообщения
    @DeleteMapping("/message/{id}")
    public ResponseEntity<HttpStatus> deleteMessage(
            //Id удаляемого сообщения
            @PathVariable Integer id
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        messageService.deleteMessage(id, username);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
