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
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    private FileUploadService fileUploadService;





    /* Get all rentals */

    public List<RentalDto> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(this::rentalToDto)
                .collect(Collectors.toList());
    }

    /* Save rental */

    public RentalDto saveRental(RentalDto rentalDto, MultipartFile imageFile) {
        Rental rental = toEntity(rentalDto,imageFile);
        rentalRepository.save(rental);

        return findRentalById(rental.getId());
    }




    /* Get rental by id.  */

    public RentalDto findRentalById(int id) {
        Rental rental = rentalRepository.findById(id).orElseThrow();
        return rentalToDto(rental);
    }



    /*Check if the current is the owner of the rental  */
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





    /*Update rental  */

    public RentalDto updateRental(RentalDto rentalDto, int id) {

        Rental existingRental = rentalRepository.findById(id).orElseThrow(()-> new RuntimeException());
        existingRental.setName(rentalDto.getName());
        existingRental.setSurface(rentalDto.getSurface());
        existingRental.setPrice(rentalDto.getPrice());
        existingRental.setDescription(rentalDto.getDescription());
        rentalRepository.save(existingRental);
        return findRentalById(id);

    }


    /*Delete rental  */
    public void deleteRental(int id) {

        Rental rental = rentalRepository.findById(id).orElseThrow(()-> new RuntimeException());
        rental.setOwner(null);
        rentalRepository.save(rental);
        rentalRepository.deleteById(id);


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
        String filepath= ServletUriComponentsBuilder.fromCurrentContextPath().path("images/"+fileName).toUriString();


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



