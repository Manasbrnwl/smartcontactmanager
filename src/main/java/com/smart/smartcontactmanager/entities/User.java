package com.smart.smartcontactmanager.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    @NotBlank(message = "Must not be Empty!!")
    @Size(min = 2, message = "Atleast 2 characters..")
    private String userName;
    @Column(unique = true)
    @NotBlank(message = "Must not be Empty!!")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String userEmail;
    @NotBlank(message = "Must not be Empty!!")
    // @Pattern(regexp =
    // "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
    // message = "Must Match = User@12345")
    private String userPassword;
    private String userRole;
    private boolean enabled;
    private String imageUrl;
    @Column(length = 1000)
    private String about;
    @AssertTrue(message = "Must be accepted!!")
    private boolean agreement;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Contact> contacts = new ArrayList<>();

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public User() {
    }

    public User(int userId, String userName, String userEmail, String userPassword, String userRole, boolean enabled,
            String imageUrl, String about, List<Contact> contacts) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userRole = userRole;
        this.enabled = enabled;
        this.imageUrl = imageUrl;
        this.about = about;
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", userEmail=" + userEmail + ", userPassword="
                + userPassword + ", userRole=" + userRole + ", enabled=" + enabled + ", imageUrl=" + imageUrl
                + ", about=" + about + ", contacts=" + contacts + "]";
    }

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

}
