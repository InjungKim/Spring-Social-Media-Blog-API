package com.example.service;

import com.example.entity.*;
import com.example.repository.MessageRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    //message repository dependency injection
    @Autowired
    MessageRepository messageRepository;

    //for adding a message
    public Message addMessage(Message message){
        return messageRepository.save(message);
    }

    //for getting a message by its id
    public Optional<Message> getMessage(Integer id){
        return messageRepository.findById(id);
    }
    
    //for getting all messages
    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    //for deleting a message by its id
    public void deleteMessage(Integer id){
        messageRepository.deleteById(id);
    }

    //for updating a message text by its id
    public void updateMessage(Integer id, String newText){
        Message oldMsg = messageRepository.findById(id).get();
        Message newMsg = new Message(id, oldMsg.getPostedBy(), newText, oldMsg.getTimePostedEpoch());
        messageRepository.save(newMsg);
    }

    //for getting all messages by account id
    public List<Message> getMessagesByAccountId(Integer id){
        return messageRepository.findByPostedBy(id);
    }
}
