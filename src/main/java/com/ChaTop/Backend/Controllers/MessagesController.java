package com.ChaTop.Backend.Controllers;

import com.ChaTop.Backend.Dto.MessageDto;


import com.ChaTop.Backend.Services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
public class MessagesController {


    @Autowired
    private MessageService messageService;



    /**
     * Send a message
     * @param MessageDto Object that contains message request attributes
     * @return Message response
     */
    @Operation(summary = "Send message")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400", description = "Bad request",
                    content = { @Content(mediaType = "application/json",examples = @ExampleObject(value="{}")) }
            ),
            @ApiResponse(
                    responseCode = "401", description = "Unauthorized",
                    content = { @Content(examples = @ExampleObject(value="")) }
            )
    })
    @RequestMapping(value="/messages", method = RequestMethod.POST)
    public Object sendMessage(@RequestBody MessageDto messageDto){


        Map<String, Object> object = new HashMap<>();

        boolean succes =false;

        if (messageService.checkRentalOwner(messageDto.getRental_id())){

                object.put("message","You are The Owner Of the rental,So you cann't send message to your self.");
                object.put("succes",succes=false);
            }
            else
            {
                messageService.sendMessage(messageDto);
                object.put("message","Message Was Sent Successfully!");
                object.put("succes",succes=true);

            }

        return object;

    }




}
