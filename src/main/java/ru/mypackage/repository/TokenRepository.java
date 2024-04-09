package ru.mypackage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mypackage.models.Token;


import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("select t from Token t where t.username = :username and t.expired = :expiration")
    List<Token> findAllValidToken(@Param("username") String username, @Param("expiration") Boolean expiration);

    Optional<Token> findByToken(String token);

}
