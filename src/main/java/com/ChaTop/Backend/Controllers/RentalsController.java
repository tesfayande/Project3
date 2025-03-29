package com.ChaTop.Backend.Controllers;

import com.ChaTop.Backend.Dto.RentalDto;
import com.ChaTop.Backend.Services.AuthService;
import com.ChaTop.Backend.Services.FileUploadService;
import com.ChaTop.Backend.Services.RentalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/rentals")
public class RentalsController {


    @Autowired
    AuthService authService;

    @Autowired
    private FileUploadService fileUploadService;


    @Autowired
   RentalService rentalService;


    /*
     Get all rentals.
    */
    @GetMapping
    public ResponseEntity<Object> getAllRentals(){


        Map<String, Object> object = new HashMap<>();


        object.put("rentals",rentalService.getAllRentals());


        return new ResponseEntity<Object>(object,HttpStatus.OK);

        //return object;
    }

    /*
     Create rental.
    */
    @PostMapping
    public ResponseEntity<RentalDto> saveRental(
            @RequestParam("name") String name,
            @RequestParam("surface") int surface,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile imageFile){


        String fileName = fileUploadService.uploadFile(imageFile);
        String filepath= ServletUriComponentsBuilder.fromCurrentContextPath().path("images/"+fileName).toUriString();

        RentalDto rentalDto= new RentalDto();


        rentalDto.setName(name);
        rentalDto.setSurface(surface);
        rentalDto.setPrice(price);
        rentalDto.setPicture(filepath);
        rentalDto.setDescription(description);


        return new ResponseEntity<RentalDto>(rentalService.saveRental(rentalDto), HttpStatus.CREATED);

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

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRental(@PathVariable("id") int id,
                                               @RequestParam("name") String name,
                                               @RequestParam("surface") int surface,
                                               @RequestParam("price") double price,
                                               @RequestParam("description") String description){


        RentalDto rentalDto= new RentalDto();


        rentalDto.setName(name);
        rentalDto.setSurface(surface);
        rentalDto.setPrice(price);
        rentalDto.setDescription(description);


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