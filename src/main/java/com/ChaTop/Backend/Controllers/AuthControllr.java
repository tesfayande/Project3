package com.ChaTop.Backend.Controllers;

import com.ChaTop.Backend.Dto.UserDto;

import com.ChaTop.Backend.Responses.AuthResponse;
import com.ChaTop.Backend.Responses.MessageResponse;
import com.ChaTop.Backend.Responses.UserResponse;
import com.ChaTop.Backend.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api")
public class AuthControllr {



    @Autowired
    AuthService authService;


    /*  Register  user */

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto){


        if (authService.checkUserEmail(userDto.getEmail())){


            return new ResponseEntity<>(new MessageResponse("Email with:"+ userDto.getEmail() + " Existes"),HttpStatus.OK);


        } else if (authService.checkUserName(userDto.getName())) {


            return new ResponseEntity<>(new MessageResponse("Name with:"+ userDto.getName() + " Existes"),HttpStatus.OK);


        } else{


            UserResponse user= authService.register(userDto);

            String userToken=authService.login(userDto);
            AuthResponse authResponse =new AuthResponse();

            authResponse.setToken(userToken);
            authResponse.setUser(user);
            authResponse.setMessage("User Created Success Fully");


            return new ResponseEntity<>(authResponse,HttpStatus.OK);



    }
    }
    /* User  login  */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto){



        if (!authService.checkUserEmail(userDto.getEmail())){


            return new ResponseEntity<MessageResponse>(new MessageResponse("Email with:"+ userDto.getEmail() + " Not Existes"),HttpStatus.NOT_FOUND);

        }else{



            UserResponse user= authService.findUserByEmail(userDto.getEmail());

            String userToken=authService.login(userDto);
            AuthResponse authResponse =new AuthResponse();

            authResponse.setToken(userToken);
            authResponse.setUser(user);
            authResponse.setMessage("User Was Logedin Successfully!");


            return new ResponseEntity<>(authResponse,HttpStatus.OK);

        }


    }

    /* Get current user */
    @GetMapping("/auth/me")
    public UserResponse me()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return  authService.findUserByEmail(authentication.getName());
        } else {
            return null;
        }

    }

    /*Get  user by id */

    @GetMapping("/user/{id}")
    // localhost:8080/api/user/1
    public ResponseEntity<UserResponse> getRentalById(@PathVariable("id") int userID){


        return new ResponseEntity<UserResponse>(authService.findUserById(userID),HttpStatus.OK);
    }


}
