package com.ChaTop.Backend.Dto;


import com.ChaTop.Backend.Models.Rental;
import com.ChaTop.Backend.Models.User;
import com.ChaTop.Backend.Responses.RentalResponse;
import com.ChaTop.Backend.Responses.UserResponse;


public class MessageDto {




    private int id;
    private int rentalId;
    private int userId;
    private RentalResponse rental;
    private UserResponse sender;
    private String message;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RentalResponse getRental() {
        return rental;
    }

    public void setRental(RentalResponse rental) {
        this.rental = rental;
    }

    public UserResponse getSender() {
        return sender;
    }

    public void setSender(UserResponse sender) {
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
