package com.ChaTop.Backend.Services;


import com.ChaTop.Backend.Dto.RentalDto;
import com.ChaTop.Backend.Models.Rental;
import com.ChaTop.Backend.Models.User;
import com.ChaTop.Backend.Repositories.RentalRepository;
import com.ChaTop.Backend.Repositories.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    private FileUploadService fileUploadService;


    public Rental createRental(Rental rental, MultipartFile imageFile) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());


        String fileName = fileUploadService.uploadFile(imageFile);
        //ServletUriComponentsBuilder.fromCurrentContextPath().path(fileName).toUriString();
        String filepath= ServletUriComponentsBuilder.fromCurrentContextPath().path("images/"+fileName).toUriString();
        //return "Upload Successfully=" + filepath;

        rental.setOwner(user);
        rental.setPicture(filepath);

        return rentalRepository.save(rental);
    }




    public Boolean checkRentalOwner(int id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        Rental rental = rentalRepository.findById(id).orElseThrow(()-> new RuntimeException());


        if(user.getId() == rental.getOwner().getId()){
            return true;
        }else {
            return false;
        }
    }


    public List<RentalDto> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(this::rentalToDto)
                .collect(Collectors.toList());
    }


    public Rental getRentalById(int id) {

        Optional<Rental> rental = rentalRepository.findById(id);
        if(rental.isPresent()){
            return rental.get();
        }else {
            throw new RuntimeException();
        }
    }


    public RentalDto findRentalById(int id) {
        Rental rental = rentalRepository.findById(id).orElseThrow();
        return rentalToDto(rental);
    }


    public RentalDto updateRental(RentalDto rentalDto, int id) {

        Rental existingRental = rentalRepository.findById(id).orElseThrow(()-> new RuntimeException());
        existingRental.setName(rentalDto.getName());
        existingRental.setSurface(rentalDto.getSurface());
        existingRental.setPrice(rentalDto.getPrice());
        existingRental.setDescription(rentalDto.getDescription());
        //existingRental.setOwner(rental.getOwner());
        // save
        rentalRepository.save(existingRental);
        return findRentalById(id);




             /*
        Comment comment = tutorialRepository.findById(tutorialId).map(tutorial -> {
            commentRequest.setTutorial(tutorial);
            return commentRepository.save(commentRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId));

        return new ResponseEntity<>(comment, HttpStatus.CREATED)
                */
    }


    public void deleteRental(int id) {

        Rental rental = rentalRepository.findById(id).orElseThrow(()-> new RuntimeException());
        rental.setOwner(null);
        rentalRepository.save(rental);
        //check
        //delete
        rentalRepository.deleteById(id);


    }



    public RentalDto saveRental(RentalDto rentalDto, MultipartFile imageFile) {
        Rental rental = toEntity(rentalDto,imageFile);
        rentalRepository.save(rental);

        return findRentalById(rental.getId());
    }



    /*
    Converting  The Rental Model  To DTO
     */

    private RentalDto rentalToDto(Rental rental) {
        RentalDto dto = new RentalDto();

        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setPicture(rental.getPicture());
        dto.setDescription(rental.getDescription());
        dto.setOwner(rental.getOwner());

        return dto;
    }


    /*
    Converting Rental DTO to  RentalDto Entity
     */
    private Rental toEntity(RentalDto rentalDto,MultipartFile imageFile) {

        Rental rental = new Rental();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        String fileName = fileUploadService.uploadFile(imageFile);
        //ServletUriComponentsBuilder.fromCurrentContextPath().path(fileName).toUriString();
        String filepath= ServletUriComponentsBuilder.fromCurrentContextPath().path("images/"+fileName).toUriString();
        //return "Upload Successfully=" + filepath;

        rental.setId(rentalDto.getId());
        rental.setName(rentalDto.getName());
        rental.setSurface(rentalDto.getSurface());
        rental.setPrice(rentalDto.getPrice());
        rental.setPicture(filepath);
        rental.setDescription(rentalDto.getDescription());
        rental.setOwner(user);
        return rental;
    }



   //Converting RentalDto Entity to  Rental Dto

    public RentalDto mapToDto(Rental rental) {

       RentalDto dto = new RentalDto();

        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setPicture(rental.getPicture());
        dto.setDescription(rental.getDescription());
        dto.setOwner(rental.getOwner());

        return dto;
    }



    //Converting Rental Tdo to  RentalDto Entity

    public Rental mapToEntity(RentalDto rentalDto) {

        Rental rental = new Rental();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());


        rental.setId(rentalDto.getId());
        rental.setName(rentalDto.getName());
        rental.setSurface(rentalDto.getSurface());
        rental.setPrice(rentalDto.getPrice());
        rental.setPicture(rentalDto.getPicture());
        rental.setDescription(rental.getDescription());
        rental.setOwner(user);
        return rental;
    }




}



