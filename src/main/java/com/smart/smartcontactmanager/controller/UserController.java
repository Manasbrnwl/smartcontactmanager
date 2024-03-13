package com.smart.smartcontactmanager.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        try {
            User user = this.userRepository.getuserByUserName(principal.getName());
            contact.setUser(user);

            // processing and uploading image or file
            if (multipartFile.isEmpty()) {
                contact.setImageUrl("default/defualt.jpg");
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

        Pageable pageable = PageRequest.of(page, 5);
        Page<Contact> contacts = this.contactRepository.findContactsByUser(userId, pageable);
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());

        return "normal\\view_contact";
    }

    @GetMapping("/contact/{currentPage}/{cId}")
    public String contactDetail(@PathVariable("cId") Integer cId, @PathVariable("currentPage") Integer page,
            Model model, Principal principal) {
        try {
            Optional<Contact> contactOptional = this.contactRepository.findById(cId);
            Contact contact = contactOptional.get();
            User user = this.userRepository.getuserByUserName(principal.getName());
            model.addAttribute("title", "Not allowed");
            if (user.getUserId() == contact.getUser().getUserId()) {
                model.addAttribute("contact", contact);
                model.addAttribute("currentPage", page);
                model.addAttribute("title", contact.getcNickName());
            }
        } catch (Exception e) {
        }
        return "normal\\contact_detail";
    }

    // details from search
    @GetMapping("/contact/{cId}")
    public String contactDetailSearch(@PathVariable("cId") Integer cId,
            Model model, Principal principal) {
        try {
            Optional<Contact> contactOptional = this.contactRepository.findById(cId);
            Contact contact = contactOptional.get();
            User user = this.userRepository.getuserByUserName(principal.getName());
            model.addAttribute("title", "Not allowed");
            if (user.getUserId() == contact.getUser().getUserId()) {
                model.addAttribute("contact", contact);
                model.addAttribute("currentPage", 0);
                model.addAttribute("title", contact.getcNickName());
            }
        } catch (Exception e) {
        }
        return "normal\\contact_detail";
    }

    @GetMapping("/contact-delete/{currentPage}/{cId}")
    public String deleteContact(@PathVariable("cId") Integer cId, @PathVariable("currentPage") Integer page,
            Principal principal, Model model,
            HttpSession session) throws IOException {
        try {
            User user = this.userRepository.getuserByUserName(principal.getName());
            // this.contactRepository.deleteById(cId);
            Optional<Contact> contacOptional = this.contactRepository.findById(cId);
            Contact contact = contacOptional.get();
            if (user.getUserId() == contact.getUser().getUserId()) {
                if (contact.getImageUrl() != "default/defualt.jpg") {
                    File file = new ClassPathResource("static/image").getFile();
                    Path path = (Path) Paths.get(file.getAbsolutePath() + File.separator + contact.getImageUrl());
                    Files.deleteIfExists(path);
                }
                this.contactRepository.delete(contact);
                session.setAttribute("message", new Message("Contact Deleted Successfully!!", "alert-success"));
                model.addAttribute("title", "View Contacts");
            }
        } catch (Exception e) {
            session.setAttribute("message", new Message("Unauthorised Access!!", "alert-danger"));
        }
        return "redirect:/user/show-contacts/" + page;
    }

    @PostMapping("/contact-update/{currentPage}/{cId}")
    public String updateContact(@PathVariable("cId") Integer cId, @PathVariable("currentPage") Integer page,
            Principal principal, Model model,
            HttpSession session) {
        try {
            User user = this.userRepository.getuserByUserName(principal.getName());
            Optional<Contact> contacOptional = this.contactRepository.findById(cId);
            Contact contact = contacOptional.get();
            if (user.getUserId() == contact.getUser().getUserId()) {
                model.addAttribute("contact", contact);
                model.addAttribute("title", "Update Contact");
                model.addAttribute("currentPage", page);
            }
        } catch (Exception e) {
            e.printStackTrace();//
        }
        return "normal\\update_contact";
    }

    @PostMapping("/process-update/{page}/{cId}")
    public String updateHandler(@ModelAttribute Contact contact, @PathVariable("page") Integer page,
            @PathVariable("cId") Integer cId, @RequestParam("profileImage") MultipartFile multipartFile, Model model,
            HttpSession session) {
        Contact oldContact = this.contactRepository.findById(cId).get();
        // this.contactRepository.save(contact);
        try {
            if (!multipartFile.isEmpty() && oldContact.getImageUrl() != "default/defualt.jpg") {
                File file = new ClassPathResource("static/image").getFile();
                Path path = (Path) Paths.get(file.getAbsolutePath() + File.separator + oldContact.getImageUrl());
                Files.deleteIfExists(path);
                Path newPath = (Path) Paths
                        .get(file.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(), newPath, StandardCopyOption.REPLACE_EXISTING);
                contact.setImageUrl(multipartFile.getOriginalFilename());
            } else {
                contact.setImageUrl(oldContact.getImageUrl());
            }
            contact.setUser(oldContact.getUser());
            contact.setcId(cId);
            this.contactRepository.save(contact);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/user/show-contacts/" + page;
    }

    // profile
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("title", "Your Profile");
        User user = this.userRepository.getuserByUserName(principal.getName());
        model.addAttribute("user", user);
        return "normal\\profile";
    }

    // setting handler

    @GetMapping("/settings")
    public String openSetting() {
        return "normal\\settings";
    }

    // change password
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword, Principal principal,
            HttpSession session) {
        User user = this.userRepository.getuserByUserName(principal.getName());
        if (this.passwordEncoder.matches(oldPassword, user.getUserPassword())) {
            if (newPassword.equals(confirmNewPassword) && !newPassword.isEmpty() && !confirmNewPassword.isEmpty()) {
                user.setUserPassword(this.passwordEncoder.encode(newPassword));
                this.userRepository.save(user);
                session.setAttribute("message",
                        new Message("Password Successfully changed!!", "alert-success"));
            } else {
                session.setAttribute("message",
                        new Message("Confirm password does not match!!", "alert-danger"));
                return "normal\\settings";
            }
        } else {
            session.setAttribute("message",
                    new Message("Password not matched with original password!!", "alert-danger"));
            return "normal\\settings";
        }
        return "redirect:/user/dashboard";
    }
}
