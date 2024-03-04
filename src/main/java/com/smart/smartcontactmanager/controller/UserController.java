package com.smart.smartcontactmanager.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // common data
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        User user = userRepository.getuserByUserName(principal.getName());
        model.addAttribute("user", user);
    }

    // home
    @RequestMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "User Dashboard");
        return "normal\\dashboard";
    }

    // add-contant
    @GetMapping("/add-contact")
    public String oenAddContactForm(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal\\add_contact";
    }

    @PostMapping("/proccess-contact")
    public String processAddContact(@ModelAttribute Contact contact, BindingResult bindingResult,
            Model model, Principal principal) {
        try {
            User user = this.userRepository.getuserByUserName(principal.getName());
            contact.setUser(user);
            user.getContacts().add(contact);
            this.userRepository.save(user);

            return "normal\\add_contact";
        } catch (Exception e) {
            e.printStackTrace();
            return "normal\\add_contact";
        }
    }

}
