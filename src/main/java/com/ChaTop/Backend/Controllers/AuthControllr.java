package com.ChaTop.Backend.Controllers;

import com.ChaTop.Backend.Dto.UserDto;

import com.ChaTop.Backend.Repositories.UserRepository;
import com.ChaTop.Backend.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController

@RequestMapping("/api")
public class AuthControllr {



    @Autowired
    AuthService authService;


    @PostMapping("/auth/register")
    public Object register(@RequestBody UserDto userDto){

        Map<String, Object> object = new HashMap<>();
        boolean succes =false;

        if (authService.checkUserEmail(userDto.getEmail())){

            object.put("message","Email with:"+ userDto.getEmail() + " Existes");
            object.put("succes",succes=false);

        } else if (authService.checkUserName(userDto.getName())) {

            object.put("message","Name with:"+ userDto.getName() + " Existes");
            object.put("succes",succes=false);

        } else{

            object.put("message","User Was Created Successfully!");
            object.put("succes",succes=true);
            object.put("data",authService.register(userDto));

        }
        return object;

    }

    @PostMapping("/auth/login")
    public Object login(@RequestBody UserDto userDto){

        Map<String, Object> object = new HashMap<>();
        boolean succes =false;

        if (!authService.checkUserEmail(userDto.getEmail())){

            object.put("message","Email with:"+ userDto.getEmail() + " Not Existes");
            object.put("succes",succes=false);
            //object.put("user",userService.getCurrentUserByName(userDto.getName()));

            return object;

        }else{

            object.put("message","User Was Logedin Successfully!");
            object.put("succes",succes=true);
            object.put("token", authService.login(userDto));
            object.put("user", authService.findUserByEmail(userDto.getEmail()));

            return object;

        }


    }


    @GetMapping("/auth/me")
    public UserDto me()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return  authService.findUserByEmail(authentication.getName());
        } else {
            return null;
        }

    }

    //Get  Rental By id
    @GetMapping("/user/{id}")
    // localhost:8080/api/auth/1
    public ResponseEntity<UserDto> getRentalById(@PathVariable("id") int userID){
        return new ResponseEntity<UserDto>(authService.findUserById(userID),HttpStatus.OK);
    }


}
