package ru.mypackage.dto.topic;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicDTO {

    @Size(min = 2, max = 20, message = "Name of topic should be between 2 and 20 characters")
    private String name;

}
