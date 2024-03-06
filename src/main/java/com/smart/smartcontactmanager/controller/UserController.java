package com.smart.smartcontactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.smartcontactmanager.dao.ContactRepository;
import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

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
    public String processAddContact(@ModelAttribute Contact contact,
            @RequestParam("profileImage") MultipartFile multipartFile,
            Model model, Principal principal, HttpSession session) {
        session.removeAttribute("message");
        try {
            User user = this.userRepository.getuserByUserName(principal.getName());
            contact.setUser(user);

            // processing and uploading image or file
            if (multipartFile.isEmpty()) {
                contact.setImageUrl("NULL");
            } else {
                contact.setImageUrl(multipartFile.getOriginalFilename());

                File file = new ClassPathResource("static/image").getFile();
                Path path = (Path) Paths
                        .get(file.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            user.getContacts().add(contact);
            this.userRepository.save(user);
            // message
            session.setAttribute("message", new Message("Contact Added Successfully!!", "success"));

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wong!!", "danger"));
        }
        return "normal\\add_contact";
    }

    // per page 5 contacts[n]
    // current page = 0[page]
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "View Contacts");
        User getuserByUserName = this.userRepository.getuserByUserName(principal.getName());
        int userId = getuserByUserName.getUserId();
        // List<Contact> contacts = getuserByUserName.getContacts();

        Pageable pageable = PageRequest.of(page, 3);
        Page<Contact> contacts = this.contactRepository.findContactsByUser(userId, pageable);
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());

        return "normal\\view_contact";
    }

}
