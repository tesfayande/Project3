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




    public Message saveMessage(Message message,int rentalID) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        Rental rental = rentalRepository.findById(rentalID).orElseThrow(()-> new RuntimeException());

        message.setSender(user);
        message.setRental(rental);

        return messageRepository.save(message);
    }



    public MessageDto sendMessage(MessageDto messageDto,int rentalId) {

        Message message =mapToEntity(messageDto,rentalId);
        messageRepository.save(message);

        return findMessageById(message.getId());


    }



    public MessageDto storeMessage(MessageDto messageDto) {

        Message message =toEntity(messageDto);
        messageRepository.save(message);

        return findMessageById(message.getId());


    }








    public List<MessageDto> getMessages() {
        return messageRepository.findAll()
                .stream()
                .map(this::messageToDto)
                .collect(Collectors.toList());
    }





    public MessageDto findMessageById(int id) {

        Message message  = messageRepository.findById(id).orElseThrow();
        return mapMessageToDto(message);


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

        //delete
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





    public Boolean checkIfRentalExists(int id) {

        //Rental rental = rentalRepository.findById(id).orElseThrow(()-> new RuntimeException());
        //rentalRepository.existsById(id);
        if(rentalRepository.existsById(id)){
            return true;
        }else {
            return false;
        }
    }


    /*
     * Using Message DTO
     *
     * */

    /*
  Converting Message Entity to  Message Dto
   */
    public MessageDto messageToDto(Message message) {
        MessageDto messageDto = new MessageDto();

        messageDto.setId(message.getId());
        messageDto.setRental(message.getRental());
        messageDto.setSender(message.getSender());
        messageDto.setMessage(message.getMessage());

        return messageDto;
    }




    /*
    Converting Message Tdo to  Message Entity
     */
    public Message toEntity(MessageDto messageDto) {
        Message message = new Message();



        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        Rental rental = rentalRepository.findById(messageDto.getRentalId()).orElseThrow(()-> new RuntimeException());

        message.setId(messageDto.getId());
        message.setRental(rental);
        message.setSender(user);
        message.setMessage(messageDto.getMessage());

        return message;
    }




    /*
  Converting Message Entity to  Message Dto
   */
    public MessageDto mapMessageToDto(Message message) {
        MessageDto messageDto = new MessageDto();

        messageDto.setId(message.getId());
        messageDto.setRentalId(message.getRental().getId());
        messageDto.setRental(message.getRental());
        messageDto.setSender(message.getSender());
        messageDto.setMessage(message.getMessage());

        return messageDto;
    }




    /*
    Converting Message Tdo to  Message Entity
     */
    public Message mapToEntity(MessageDto messageDto,int rentalId) {
        Message message = new Message();



        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        //Rental rental = rentalRepository.findById(rentalId).orElseThrow(()-> new RuntimeException());

        message.setId(messageDto.getId());
        message.setRental(messageDto.getRental());
        message.setSender(user);
        message.setMessage(messageDto.getMessage());

        return message;
    }

}
