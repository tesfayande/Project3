package com.ChaTop.Backend.Controllers;

import com.ChaTop.Backend.Dto.MessageDto;


import com.ChaTop.Backend.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessagesController {


    @Autowired
    private MessageService messageService;



    /*
    Get all messages
     */
    @GetMapping
    public List<MessageDto> getMessages(){
        return messageService.getMessages();
    }




    /*
   Send message if the current user is not owner of the rental that associated with the message.
     */

    @PostMapping("/send")
    public Object sentMessage(@RequestBody MessageDto messageDto){


        Map<String, Object> object = new HashMap<>();
        boolean succes =false;


        if (messageService.checkIfRentalExists(messageDto.getRentalId()))
        {
            if (messageService.checkRentalOwner(messageDto.getRentalId())){

                object.put("message","You are The Owner Of the rental,So you cann't send message to your self.");
                object.put("succes",succes=false);
            }
            else
            {
                object.put("message","Message Was Sent Successfully!");
                object.put("succes",succes=true);
                object.put("data", messageService.saveMessage(messageDto));
            }
        } else
        {
            object.put("message","The Rental you are looking is not exists.");
            object.put("succes",succes=false);
        }
        return object;

    }



    /*
    Get message by id

     */

    @GetMapping("/show/{id}")
    // localhost:8080/api/messages/1
    public ResponseEntity<MessageDto> getMessageById(@PathVariable("id") int messageID){

        Map<String, Object> object = new HashMap<>();
        boolean succes =false;

        return new ResponseEntity<MessageDto>(messageService.findMessageById(messageID),HttpStatus.OK);
    }

    /*
    Update message if the current user is the sender of the message
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateMessage(@PathVariable("id") int id,
                                                 @RequestBody MessageDto messageDto){

        Map<String, Object> object = new HashMap<>();
        boolean succes =false;



        if (messageService.checkSender(id)){

            object.put("message","Message  Was Updated Successfully!");
            object.put("succes",succes=true);
            object.put("data", messageService.updateMessage(messageDto,id));

        }
        else
        {
            object.put("message","You are Not The Sendr Of the message,So you cann't Update.");
            object.put("succes",succes=false);


        }


        return new ResponseEntity<Object>(object,HttpStatus.OK);
    }


    /*
    Delete message if the current user is The sender of the message.
     */

    @DeleteMapping("/delete/{id}")
    public Object deleteMessage(@PathVariable("id") int id){

        Map<String, Object> object = new HashMap<>();
        boolean succes =false;



        if (messageService.checkSender(id)){
            messageService.deleteMessage(id);
            object.put("message","Message  Was Delete Successfully!");
            object.put("succes",succes=true);

        }
        else
        {
            object.put("message","You are Not The Sender Of the message,So you cann't Delete it.");
            object.put("succes",succes=false);

        }
         return new ResponseEntity<Object>(object,HttpStatus.OK);


    }



}
