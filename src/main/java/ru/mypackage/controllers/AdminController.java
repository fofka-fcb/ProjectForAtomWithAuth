package ru.mypackage.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.dto.user.UserDTO;
import ru.mypackage.dto.user.UsersResponseDTO;
import ru.mypackage.dto.message.MessageDTO;
import ru.mypackage.dto.topic.TopicDTO;
import ru.mypackage.exceptions.MessageDTOValidationException;
import ru.mypackage.exceptions.TopicDTOValidationException;
import ru.mypackage.services.MessageService;
import ru.mypackage.services.TopicService;
import ru.mypackage.services.UserService;
import ru.mypackage.utils.TopicDTOValidator;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin")
public class AdminController {

    private final MessageService messageService;
    private final TopicService topicService;
    private final UserService userService;
    private final TopicDTOValidator topicDTOValidator;

    @Autowired
    public AdminController(MessageService messageService,
                           TopicService topicService,
                           UserService userService,
                           TopicDTOValidator topicDTOValidator
    ) {
        this.messageService = messageService;
        this.topicService = topicService;
        this.userService = userService;
        this.topicDTOValidator = topicDTOValidator;
    }

    //Получение всех пользователей
    @GetMapping("/users")
    public ResponseEntity<UsersResponseDTO> getAllUsers() {
        UsersResponseDTO usersResponseDTO = new UsersResponseDTO(
                userService.findAll()
        );

        return new ResponseEntity<>(usersResponseDTO, HttpStatus.OK);
    }

    //Получение одного пользователя по id
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(
            @PathVariable Integer id
    ) {
        return new ResponseEntity<>(userService.findOneById(id), HttpStatus.OK);
    }

    //Изменение имени топика
    @PatchMapping("/topic/{id}")
    public ResponseEntity<HttpStatus> refTopicName(
            @PathVariable Integer id,
            @RequestBody @Valid TopicDTO topicDTO,
            BindingResult bindingResult
    ) {
        topicDTOValidator.validate(topicDTO, bindingResult);

        if (bindingResult.hasErrors())
            throw new TopicDTOValidationException(bindingResult);

        topicService.updateTopicName(id, topicDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Изменение содержимого сообщения
    @PatchMapping("/message/{id}")
    public ResponseEntity<HttpStatus> refMessage(
            @PathVariable Integer id,
            @RequestBody @Valid MessageDTO messageDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors())
            throw new MessageDTOValidationException(bindingResult);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        messageService.updateMessage(id, messageDTO, username);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Удаление топика по id
    @DeleteMapping("/topic/{id}")
    public ResponseEntity<HttpStatus> deleteTopic(
            @PathVariable Integer id
    ) {
        topicService.deleteTopic(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Удаление сообщения по id
    @DeleteMapping("/message/{id}")
    public ResponseEntity<HttpStatus> deleteMessage(
            @PathVariable Integer id
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        messageService.deleteMessage(id, username);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
