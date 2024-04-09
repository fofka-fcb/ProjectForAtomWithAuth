package ru.mypackage.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mypackage.models.ApplicationUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"authorities"})
    Optional<ApplicationUser> findByUsername(String username);

    @EntityGraph(attributePaths = {"authorities"})
    List<ApplicationUser> findAll();

    Optional<ApplicationUser> findByUsernameStartingWith(String username);

    Optional<ApplicationUser> findById(int id);

}
