package com.ChaTop.Backend.Services;

import com.ChaTop.Backend.Dto.MessageDto;
import com.ChaTop.Backend.Models.Message;
import com.ChaTop.Backend.Models.Rental;
import com.ChaTop.Backend.Models.User;
import com.ChaTop.Backend.Repositories.MessageRepository;
import com.ChaTop.Backend.Repositories.RentalRepository;
import com.ChaTop.Backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RentalRepository rentalRepository;




    public Message sendMessage(MessageDto messageDto){

        User user = userRepository.findById(messageDto.getUser_id()).orElseThrow(()-> new RuntimeException());

        Rental rental = rentalRepository.findById(messageDto.getRental_id()).orElseThrow(()-> new RuntimeException());

        Message message = new Message();
        message.setMessage(messageDto.getMessage());
        message.setSender(user);
        message.setRental(rental);


        return messageRepository.save(message);
    }







    public List<MessageDto> getMessages() {
        return messageRepository.findAll()
                .stream()
                .map(this::messageToDto)
                .collect(Collectors.toList());
    }





    public MessageDto findMessageById(int id) {

        Message message  = messageRepository.findById(id).orElseThrow();
        return messageToDto(message);


    }





    public Message updateMessage(MessageDto messageDto, int id) {

        Message existingMessage = messageRepository.findById(id).orElseThrow(()-> new RuntimeException());
        existingMessage.setMessage(messageDto.getMessage());
        messageRepository.save(existingMessage);
        return existingMessage;
    }


    public void deleteMessage(int id) {

        Message message = messageRepository.findById(id).orElseThrow(()-> new RuntimeException());
        message.setRental(null);
        message.setSender(null);
        messageRepository.save(message);
        messageRepository.deleteById(id);
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



    public Boolean checkSender(int id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        Message message = messageRepository.findById(id).orElseThrow(()-> new RuntimeException());


        if(user.getId() == message.getSender().getId()){
            return true;
        }else {
            return false;
        }
    }



     /*
    Check if rental exists before send
     */

    public Boolean checkIfRentalExists(int id) {

        if(rentalRepository.existsById(id)){
            return true;
        }else {
            return false;
        }
    }




    /*
  Converting Message Entity to  Message Dto
   */
    public MessageDto messageToDto(Message message) {
        MessageDto messageDto = new MessageDto();

        messageDto.setId(message.getId());
        messageDto.setRental_id(message.getRental().getId());
        messageDto.setRental_id(message.getSender().getId());
        messageDto.setMessage(message.getMessage());

        return messageDto;
    }




    /*
    Converting Message Tdo to  Message Entity
     */
    public Message messageToEntity(MessageDto messageDto) {


        Message message = new Message();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        Rental rental = rentalRepository.findById(messageDto.getRental_id()).orElseThrow(()-> new RuntimeException());

        message.setId(messageDto.getId());
        message.setRental(rental);
        message.setSender(user);
        message.setMessage(messageDto.getMessage());

        return message;
    }



}
