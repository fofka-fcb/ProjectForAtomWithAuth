package ru.mypackage.dto.topic;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mypackage.dto.message.MessageDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTopicDTO {

    @Valid
    private TopicDTO topicDTO;

    @Valid
    private MessageDTO messageDTO;

}
