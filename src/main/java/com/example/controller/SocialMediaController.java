package com.example.controller;

import com.example.entity.*;
import com.example.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    //account service and message service dependency injection
    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    //mapping for registering a user
    @PostMapping(value = "/register")
    public ResponseEntity postAccount(@RequestBody Account account){
        //check username and password conditions
        if(account.getUsername() != null && account.getUsername().length() > 0 && account.getPassword().length() >= 4){
            //get a list of accounts by username, there should be a maximum of 1
            List<Account> list = accountService.getAccountsByUsername(account.getUsername());
            if(list.isEmpty()){
                //there is no account by this username
                Account addedAccount = accountService.addAccount(account);
                if(addedAccount != null){
                    //saved successfully return with status code 200 and the added account in the response body
                    return ResponseEntity.status(200).body(addedAccount);
                }else{
                    //not saved successfully any reason return with status code 400
                    return ResponseEntity.status(400).body(null);
                }
            }else{
                //there already exists and account with given username, thus return with status code 409
                return ResponseEntity.status(409).body(null);
            }
        }
        //not saved successfully for another reason
        return ResponseEntity.status(400).body(null);
    }

    //mapping for authenticating a login
    @PostMapping(value = "/login")
    public ResponseEntity postLogin(@RequestBody Account account){
        //get a list of accounts by username and password, there should be a maximum of 1
        List<Account> list = accountService.getAccountsByUsernameAndPassword(account.getUsername(), account.getPassword());
        if(!list.isEmpty()){
            //found an account with this username and password, return with status code 200 and the account in the response body
            return ResponseEntity.status(200).body(list.get(0));
        }
        //failed for any reason, return with status code 401
        return ResponseEntity.status(401).body(null);
    }

    //mapping for adding a message
    @PostMapping(value = "/messages")
    public ResponseEntity postMessage(@RequestBody Message message){
        //check message text conditions
        if(message.getMessageText() != null && !message.getMessageText().equals("") && message.getMessageText().length() <= 255){
            //check if there is an account by the postedby account id
            Account acc = accountService.getAccount(message.getPostedBy());
            if(acc != null){
                //if the account does exist add the message to the database
                Message addedMessage = messageService.addMessage(message);
                if(addedMessage != null){
                    //saved successfully exit with status code 200 and the added message in the response body
                    return ResponseEntity.status(200).body(addedMessage);
                }
            }
        }
        //not saved successfully for another reason, exit with status code 400
        return ResponseEntity.status(400).body(null);
    }

    //mapping for getting all messages
    @GetMapping(value = "/messages")
    public ResponseEntity getMessages(){
        //retrieve all messages from database and exit with status code 200 and the list in the response body
        List<Message> list = messageService.getAllMessages();
        return ResponseEntity.status(200).body(list);
    }

    //mapping for getting a message by its id
    @GetMapping(value = "/messages/{messageId}")
    public ResponseEntity getMessageById(@PathVariable int messageId){
        //wrapped in optional class because message service uses findById()
        Optional<Message> message = messageService.getMessage(messageId);
        //message exists, return with status code 200 and the message in the response body
        if(message.isPresent()) return ResponseEntity.status(200).body(message.get());
        //message does not exist, return with status code 200 and nothing in the response body
        return ResponseEntity.status(200).body(null);
    }

    //mapping for deleting a message by its id
    @DeleteMapping(value = "/messages/{messageId}")
    public ResponseEntity deleteMessageById(@PathVariable int messageId){
        //wrapped in optional class because message service uses findById()
        Optional<Message> message = messageService.getMessage(messageId);
        if(message.isPresent()) {
            //message does exist, delete the message and exist with status code 200 and the number of rows affected (is always 1)
            messageService.deleteMessage(messageId);
            return ResponseEntity.status(200).body(1);
        }
        //message was not present, exit with status code 200 and empty response body
        return ResponseEntity.status(200).body(null);
    }

    //mapping for updating a message text by its id
    @PatchMapping(value = "/messages/{messageId}")
    public ResponseEntity updateMessageById(@PathVariable int messageId, @RequestBody Message msg){
        String newText = msg.getMessageText();
        //check if the new text is in acceptable format
        if(newText == null || newText.isBlank() || newText.length() > 255) 
            return ResponseEntity.status(400).body(null);

        //wrapped in optional class because message service uses findById()
        Optional<Message> message = messageService.getMessage(messageId);
        if(message.isPresent()) {
            //message does exist, update the message text and return with status code 200 and the number of rows affected (always 1)
            messageService.updateMessage(messageId, newText);
            return ResponseEntity.status(200).body(1);
        }
        //message does not exist, return with status code 400 and nothing in the response body
        return ResponseEntity.status(400).body(null);
    }

    //mapping for getting all messages by user id
    @GetMapping(value = "/accounts/{accountId}/messages")
    public ResponseEntity getMessagesByUserId(@PathVariable int accountId){
        //get all messages by user id and return with status code 200 and the list in the response body
        List<Message> list = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.status(200).body(list);
    }
}
