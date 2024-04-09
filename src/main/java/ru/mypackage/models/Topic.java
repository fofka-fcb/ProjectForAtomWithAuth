package ru.mypackage.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "topic")
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private List<Message> listOfMessages;

    public Topic() {
    }

    public Topic(String name, List<Message> listOfMessages) {
        this.name = name;
        this.listOfMessages = listOfMessages;
    }

    public Topic(Integer id, String name, Date createdAt, List<Message> listOfMessages) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.listOfMessages = listOfMessages;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Message> getListOfMessages() {
        return listOfMessages;
    }

    public void setListOfMessages(List<Message> listOfMessages) {
        this.listOfMessages = listOfMessages;
    }
}
