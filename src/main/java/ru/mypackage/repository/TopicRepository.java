package ru.mypackage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mypackage.models.Topic;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @EntityGraph(attributePaths = {"listOfMessages"})
    Page<Topic> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"listOfMessages"})
    List<Topic> findAll(Sort sort);

    Optional<Topic> findByName(String name);

}
