package com.ChaTop.Backend.Models;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;




@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name",unique=true)
    private String name;



    @Column(name="email",unique=true)
    private String email;

    @Column(name="password")
    private String password;


    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "updated_at")
    private Instant UpdatedAt;


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


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }



    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }



    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
    
}

