package ru.mypackage.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mypackage.models.Message;
import ru.mypackage.models.Topic;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @EntityGraph(attributePaths = {"topic"})
    List<Message> findAllByTopic(Topic topic);

    @EntityGraph(attributePaths = {"topic"})
    List<Message> findAllByTopic(Topic topic, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from Message m where m.topic = :topic")
    void deleteAllByTopic(@Param("topic") Topic topic);

    @Modifying
    @Transactional
    @Query("delete from Message m where m.id = :id")
    void delete(@Param("id") Integer id);

}
