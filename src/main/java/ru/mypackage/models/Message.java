package ru.mypackage.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "message")
    private String message;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "id_of_topic", referencedColumnName = "topic_id")
    private Topic topic;

    public Message() {
    }

    public Message(String username, String message, Date createdAt, Topic topic) {
        this.username = username;
        this.message = message;
        this.createdAt = createdAt;
        this.topic = topic;
    }

    public Message(Integer id, String username, String message, Date createdAt, Topic topic) {
        this.id = id;
        this.username = username;
        this.message = message;
        this.createdAt = createdAt;
        this.topic = topic;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getTopicId() {
        return topic.getId();
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
