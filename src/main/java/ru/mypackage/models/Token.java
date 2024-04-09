package ru.mypackage.models;


import jakarta.persistence.*;
import ru.mypackage.models.enums.TokenType;

@Entity
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "token")
    private String token;

    @Column(name = "expired")
    private Boolean expired;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TokenType tokenType = TokenType.BEARER;

    public Token() {
    }

    public Token(Integer id, String username, String token, Boolean expired, TokenType tokenType) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.expired = expired;
        this.tokenType = tokenType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

}
