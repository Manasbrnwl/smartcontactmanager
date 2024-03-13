package com.smart.smartcontactmanager.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.smartcontactmanager.config.EmailService;
import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    int vOtp = 0;

    @GetMapping("/forget-password")
    public String forgetPassword(Model model) {
        model.addAttribute("title", "Forget Password - Smart Contact Manager");
        model.addAttribute("verifyEmail", "false");
        return "forget_password";
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email, HttpSession session, Model model) {
        boolean isAvailable = this.userRepository.existsByUserEmail(email);
        if (isAvailable) {
            // generating otp for 6 digit
            vOtp = new Random().nextInt(100000, 1000000);
            String message = "We received a request to reset your password for your Smart Contact Manager account. \r\n"
                    + "Your One-Time Password (OTP) is: " + vOtp + "\r\n"
                    + "Please use this OTP to proceed with changing your password."
                    + "If you did not request a password reset, please ignore this email or contact our support team immediately.";
            boolean flag = this.emailService.sendEmail(message, "OTP to change password at Smart Contact Manager",
                    email);
            if (flag) {
                model.addAttribute("verifyEmail", "true");
                model.addAttribute("email", email);
                // model.addAttribute("otp", passwordEncoder.encode(String.valueOf(otp)));
                session.setAttribute("message", new Message("OTP send!!", "alert-success"));
            } else {
                session.setAttribute("message", new Message("OTP not send!!  Try Again!!", "alert-danger"));
                model.addAttribute("verifyEmail", "false");
            }
        } else {
            session.setAttribute("message", new Message("Wrong Email Address!!", "alert-danger"));
            model.addAttribute("verifyEmail", "false");
        }
        return "forget_password";
    }

    @PostMapping("/change-password/{email}")
    public String changePassword(@RequestParam("otp") int otp,
            @PathVariable("email") String email, Model model, HttpSession session) {
        // if (passwordEncoder.matches(otp, vOtp)) {
        if (vOtp == otp) {
            model.addAttribute("email", email);
            return "change_password";
        } else {
            model.addAttribute("email", email);
            session.setAttribute("message", new Message("Wrong otp!!", "alert-danger"));
            return "forget_password";
        }
    }

    @PostMapping("/change/{email}")
    public String change(@PathVariable("email") String email, @RequestParam("Password") String password,
            @RequestParam("cPassword") String cpassword, HttpSession session) {
        if (password.equals(cpassword) && !password.isEmpty() && !cpassword.isEmpty()) {
            User user = this.userRepository.getuserByUserName(email);
            user.setUserPassword(this.passwordEncoder.encode(password));
            this.userRepository.save(user);
            session.setAttribute("message", new Message("Your Password Has Changed Successfully!!", "alert-success"));
            return "redirect:/signin";
        } else {
            session.setAttribute("message", new Message("Password does not matched!!", "alert-danger"));
            return "change_password";
        }
    }
}
