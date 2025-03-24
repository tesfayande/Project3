package com.ChaTop.Backend.Dto;


import com.ChaTop.Backend.Models.Rental;
import com.ChaTop.Backend.Models.User;









public class MessageDto {




    private int id;
    private int rentalId;
    private Rental rental;
    private User sender;
    private String message;


    
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

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }
}
