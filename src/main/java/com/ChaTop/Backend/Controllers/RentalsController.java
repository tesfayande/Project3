package com.ChaTop.Backend.Controllers;

import com.ChaTop.Backend.Dto.RentalDto;
import com.ChaTop.Backend.Services.AuthService;
import com.ChaTop.Backend.Services.RentalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/rentals")
public class RentalsController {


    @Autowired
    AuthService authService;

    @Autowired
   RentalService rentalService;


    /*
     Get all rentals.
    */
    @GetMapping
    public List<RentalDto> getAllRentals(){

        return rentalService.getAllRentals();
    }

    /*
     Create rental.
    */
    @PostMapping("/save")
    public ResponseEntity<RentalDto> saveRental(@ModelAttribute RentalDto rentalDto,@RequestParam("image") MultipartFile imageFile){

        return new ResponseEntity<RentalDto>(rentalService.saveRental(rentalDto, imageFile), HttpStatus.CREATED);

    }


    /*
     Get  rental by id.
    */

    @GetMapping("/{id}")
    // localhost:8080/api/rentals/1
    public ResponseEntity<RentalDto> getRentalById(@PathVariable("id") int rentalID){
        return new ResponseEntity<RentalDto>(rentalService.findRentalById(rentalID),HttpStatus.OK);
    }


    /*
   Update rental if the current user is the owner of the rental.
   */

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateRental(@PathVariable("id") int id,
                                               @RequestBody RentalDto rentalDto){


        Map<String, Object> object = new HashMap<>();
        boolean succes =false;


        if (rentalService.checkRentalOwner(id)){

            object.put("message","Rental  Was Updated Successfully!");
            object.put("succes",succes=true);
            object.put("data", rentalService.updateRental(rentalDto,id));

        }
        else
        {
            object.put("message","You are Not The Owner Of the rental,So you cann't Update.");
            object.put("succes",succes=false);
        }

        return new ResponseEntity<Object>(object,HttpStatus.OK);
    }

    /*
    Delete rental if the current user is the owner of the rental.
     */

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteRental(@PathVariable("id") int id){

        Map<String, Object> object = new HashMap<>();
        boolean succes =false;

        if (rentalService.checkRentalOwner(id)){

            rentalService.deleteRental(id);
            object.put("message","Rental  Was Deleted Successfully!");
            object.put("succes",succes=true);


        }
        else
        {
            object.put("message","You are Not The Owner Of the rental,So you cann't Delete It.");
            object.put("succes",succes=false);
        }

        return new ResponseEntity<Object>(object,HttpStatus.OK);
    }

}