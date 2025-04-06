package com.example.p8t2adrianaflorinroxana.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "Messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(name = "message_text", nullable = false)
    private String text;

    @Column(name = "sent_at", nullable = false)
    private LocalTime sentAt;

    @Column(name = "read_at", nullable = false)
    private LocalTime readAt;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chats chat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users sender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalTime readAt) {
        this.readAt = readAt;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Chats getChat() {
        return chat;
    }

    public void setChat(Chats chat) {
        this.chat = chat;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }
}
