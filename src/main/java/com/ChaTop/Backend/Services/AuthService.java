package com.ChaTop.Backend.Services;


import com.ChaTop.Backend.Dto.UserDto;
import com.ChaTop.Backend.Models.User;
import com.ChaTop.Backend.Responses.UserResponse;
import com.ChaTop.Backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    @Autowired
    UserRepository userRepository;

    @Autowired

    JWTService jwtService;
    @Autowired
    AuthenticationManager authManager;






    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    /*User Register */

    public UserResponse register(UserDto userDto){


        User dto = userToEntity(userDto);
        User userEntity = userRepository.findByEmail(userDto.getEmail());
        if (userEntity == null) {
            userRepository.save(dto);
            return findUserById(dto.getId());
        }
        else
        {
            throw new UsernameNotFoundException("User with Email \"" + userDto.getEmail() + "\" is taken found!" );
        }




    }

    /*User Login */
    public String login(UserDto userDto) {

        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(),userDto.getPassword()));

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(userDto.getEmail());
        }else{
            return "failure";
        }


    }

     /*Check If  User Email is Taken */

    public Boolean checkUserEmail(String email) {


        User user =userRepository.findByEmail(email);

        if(user ==null){
            return false;
        }else {
            return true;
        }
    }





       /*Check If  User Name is Taken */
    public Boolean checkUserName(String name) {


        User user =userRepository.findByName(name);

        if(user ==null){
            return false;
        }else {
            return true;
        }
    }


      /*Check If  User Is Authenticated */
    public Boolean userAuthenticated(UserDto userDto) {

        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(),userDto.getPassword()));

        if(authentication.isAuthenticated()){
            return false;
        }else {
            return true;
        }
    }

    /*Get User By Name */
    public User findUserByName(String name) {

        return userRepository.findByName(name);
    }


      /*Get User By Email */
    public UserResponse findUserByEmail(String email) {

        User user =userRepository.findByEmail(email);
        return userResponse(user);

    }

       /*Get User By ID */
    public UserResponse findUserById(int id) {

        User user  = userRepository.findById(id).orElseThrow();
        return userResponse(user);


    }

      /*
      User update
       */


    public UserResponse updateUser(UserDto userDto,int id) {

        User existingUser = userRepository.findById(id).orElseThrow(()-> new RuntimeException());
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(encoder.encode(userDto.getPassword()));
        userRepository.save(existingUser);
        return findUserById(userDto.getId());





    }

    /*
      User Change Password
       */

    public UserResponse updateUserPassWord(UserDto userDto, int id) {

        User existingUser = userRepository.findById(id).orElseThrow(()-> new RuntimeException());
        existingUser.setPassword(encoder.encode(userDto.getPassword()));
        userRepository.save(existingUser);
        return findUserById(id);
    }


    /*
      User Change Email
       */

    public UserResponse updateUserEmail(UserDto userDto, int id) {

        User existingUser = userRepository.findById(id).orElseThrow(()-> new RuntimeException());
        existingUser.setEmail(userDto.getEmail());
        userRepository.save(existingUser);
        return findUserById(id);
    }
      /*
    User Change Name
       */


    public UserResponse updateUserName(UserDto user, int id) {

        User existingUser = userRepository.findById(id).orElseThrow(()-> new RuntimeException());
        existingUser.setName(user.getName());
        userRepository.save(existingUser);

        return findUserById(id);
    }


    /*
    Delete User
       */

    public void deleteUser(int id) {

        //check
        userRepository.findById(id).orElseThrow(()-> new RuntimeException());
        //delete
        userRepository.deleteById(id);
    }


    /*
  Converting User Entity to  User Dto
  */
    public UserResponse userResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());;
        response.setEmail(user.getEmail());
        //dto.setPassword(user.getPassword());
        return response;
    }


    /*
   Converting User Entity to  User Dto
   */
    public UserDto userToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());;
        dto.setEmail(user.getEmail());
        //dto.setPassword(user.getPassword());
        return dto;
    }


     /*
     Converting User Dto to  User Entity
      */

    public User userToEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        return user;
    }





}
