package com.smart.smartcontactmanager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cId;
    private String cFirstName;
    private String cSecondName;
    private String cNickName;
    private String cEmail;
    private String cWork;
    private String cPhone;
    @Column(length = 1000)
    private String cInfo;
    private String imageUrl;

    @ManyToOne
    @JsonIgnore
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getcFirstName() {
        return cFirstName;
    }

    public void setcFirstName(String cFirstName) {
        this.cFirstName = cFirstName;
    }

    public String getcSecondName() {
        return cSecondName;
    }

    public void setcSecondName(String cSecondName) {
        this.cSecondName = cSecondName;
    }

    public String getcNickName() {
        return cNickName;
    }

    public void setcNickName(String cNickName) {
        this.cNickName = cNickName;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcWork() {
        return cWork;
    }

    public void setcWork(String cWork) {
        this.cWork = cWork;
    }

    public String getcPhone() {
        return cPhone;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }

    public String getcInfo() {
        return cInfo;
    }

    public void setcInfo(String cInfo) {
        this.cInfo = cInfo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Contact() {
    }

    public Contact(int cId, String cFirstName, String cSecondName, String cNickName, String cEmail, String cWork,
            String cPhone, String cInfo,
            String imageUrl, User user) {
        this.cId = cId;
        this.cFirstName = cFirstName;
        this.cSecondName = cSecondName;
        this.cNickName = cNickName;
        this.cEmail = cEmail;
        this.cWork = cWork;
        this.cPhone = cPhone;
        this.cInfo = cInfo;
        this.imageUrl = imageUrl;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Contact [cId=" + cId + ", cFirstName=" + cFirstName + ", cSecondName=" + cSecondName + ", cNickName="
                + cNickName + ", cEmail=" + cEmail + ", cWork=" + cWork + ", cPhone=" + cPhone + ", cInfo=" + cInfo
                + ", imageUrl=" + imageUrl + ", user=" + user + "]";
    }

}
