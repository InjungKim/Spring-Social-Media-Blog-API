package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.entity.*;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    //method for getting all accounts by given username
    List<Account> findByUsername(String username);
    //method for getting all accounts by given username AND password
    List<Account> findByUsernameAndPassword(String username, String password);
}
