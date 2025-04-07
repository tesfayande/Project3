package com.ChaTop.Backend.Controllers;

import com.ChaTop.Backend.Dto.RentalDto;
import com.ChaTop.Backend.Responses.RentalResponse;
import com.ChaTop.Backend.Responses.RentalsResponse;
import com.ChaTop.Backend.Services.AuthService;
import com.ChaTop.Backend.Services.FileUploadService;
import com.ChaTop.Backend.Services.RentalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;


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
    public RentalsResponse getAllRentals(){



       return new RentalsResponse(this.rentalService.getAllRentals());


    }

    /*
     Create rental.
    */
    @PostMapping
    public RentalResponse saveRental(
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



        rentalService.saveRental(rentalDto);
        return new RentalResponse("Rental  Was Created Successfully!");




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
    public RentalResponse updateRental(@PathVariable("id") int id,
                                               @RequestParam("name") String name,
                                               @RequestParam("surface") int surface,
                                               @RequestParam("price") double price,
                                               @RequestParam("description") String description){


        RentalDto rentalDto= new RentalDto();


        rentalDto.setName(name);
        rentalDto.setSurface(surface);
        rentalDto.setPrice(price);
        rentalDto.setDescription(description);




        if (rentalService.checkRentalOwner(id)){

            rentalService.updateRental(rentalDto,id);
            return new RentalResponse("Rental  Was Updated Successfully!");
        }
        else
        {

            return new RentalResponse("You are Not The Owner Of the rental,So you cann't Update.");
        }


    }

    /*
    Delete rental if the current user is the owner of the rental.
     */

    @DeleteMapping("/delete/{id}")
    public RentalResponse deleteRental(@PathVariable("id") int id){


        if (rentalService.checkRentalOwner(id)){

            rentalService.deleteRental(id);

            return new RentalResponse("Rental  Was Deleted Successfully!");


        }
        else
        {
            return new RentalResponse("You are Not The Owner Of the rental,So you cann't Delete It.");
        }

    }

}