package com.ChaTop.Backend.Controllers;

import com.ChaTop.Backend.Dto.MessageDto;


import com.ChaTop.Backend.Dto.RentalDto;
import com.ChaTop.Backend.Requests.MessageRequest;
import com.ChaTop.Backend.Responses.MessageResponse;
import com.ChaTop.Backend.Services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
public class MessagesController {


    @Autowired
    private MessageService messageService;



    /**
     * Send a message
     * @param newMessageReq Object that contains message request attributes
     * @return Message response
     */
    @Operation(summary = "Send message")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageResponse.class)
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
    public Object sendMessage(@RequestBody MessageRequest newMessageReq){


        Map<String, Object> object = new HashMap<>();

        boolean succes =false;

        if (messageService.checkRentalOwner(newMessageReq.getRental_id())){

                object.put("message","You are The Owner Of the rental,So you cann't send message to your self.");
                object.put("succes",succes=false);
            }
            else
            {
                messageService.sendMessage(newMessageReq);
                object.put("message","Message Was Sent Successfully!");
                object.put("succes",succes=true);

            }

        return object;

    }




}
