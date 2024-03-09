package com.smart.smartcontactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.smartcontactmanager.dao.ContactRepository;
import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;

@RestController // used to retun json or the retuen element as it is
public class SearchController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/user/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal) {
        User user = this.userRepository.getuserByUserName(principal.getName());
        List<Contact> contacts = this.contactRepository.findByCNicknameContainingAndUser(query, user);
        return ResponseEntity.ok(contacts);
    }
}
