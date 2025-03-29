package com.ChaTop.Backend.Models;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;




@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "rental_id",nullable =true)
    private Rental rental;




    
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "user_id",nullable =true)
    private User sender;





    @Column(name = "message")
    private String message;


    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at")
    private Instant createdAt;


    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "updated_at")
    private Instant UpdatedAt;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User user) {
        this.sender= user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        UpdatedAt = updatedAt;
    }
}
