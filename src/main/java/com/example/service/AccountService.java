package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.stereotype.Service;
import org.aspectj.internal.lang.annotation.ajcDeclarePrecedence;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class AccountService {
    //account repository dependency injection
    @Autowired
    private AccountRepository accountRepository;

    //for adding account, uses save() method
    public Account addAccount(Account account){
        return accountRepository.save(account);
    }

    //for getting an account by id, uses getById() method
    public Account getAccount(int id){
        if(accountRepository.existsById(id)){
            return accountRepository.getById(id);
        }else{
            return null;
        }
    }

    //for getting all accounts by username, uses findByUsername() method
    public List<Account> getAccountsByUsername(String username){
        return accountRepository.findByUsername(username);
    }

    //for getting all accounts by username and password, uses findByUsernameAndPassword() method
    public List<Account> getAccountsByUsernameAndPassword(String username, String password){
        return accountRepository.findByUsernameAndPassword(username, password);
    }
}
