package ru.mypackage.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.mypackage.dto.topic.TopicDTO;
import ru.mypackage.repository.TopicRepository;


@Component
public class TopicDTOValidator implements Validator {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicDTOValidator(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return TopicDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TopicDTO topicDTO = (TopicDTO) target;

        if (topicRepository.findByName(topicDTO.getName()).isPresent()) {
            errors.rejectValue("topicDTO", "", "This name of topic is already taken!");
        }
    }
}
