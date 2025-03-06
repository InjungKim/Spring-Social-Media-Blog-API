package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.*;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer>{
    //method for getting all messages by postedBy aka accountId
    List<Message> findByPostedBy(Integer accountId);
}
