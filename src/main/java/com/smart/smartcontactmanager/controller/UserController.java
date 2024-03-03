package com.smart.smartcontactmanager.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.User;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        User user = userRepository.getuserByUserName(principal.getName());
        model.addAttribute("user", user);
        return "normal\\dashboard";
    }
}
