package com.smart.smartcontactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    // registering user
    @PostMapping("/do_register")
    public String user(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model,
            HttpSession session) {
        session.removeAttribute("message");
        try {
            // if (!user.isAgreement()) {
            // throw new Exception("You have not agreed terms and condition !!!");
            // }
            if (bindingResult.hasErrors()) {
                model.addAttribute("user", user);
                return "signup";
            }

            user.setUserRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("default.jpg");
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
            System.out.println("Agreement = " + user.isAgreement());
            User result = this.userRepository.save(user);
            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully Registered !!! ", "alert-success"));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something Went Wrong !!! " + e.getMessage(), "alert-danger"));
        }
        return "signup";
    }

    @GetMapping("/signin")
    public String customLogin(Model model) {
        model.addAttribute("title", "Log In - Smart Contact Manager");
        return "login";
    }
}
