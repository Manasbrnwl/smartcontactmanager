package com.smart.smartcontactmanager.entities;

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
    private String cName;
    private String cNickName;
    @Column(unique = true)
    private String cEmail;
    private String cWork;
    private String cPhone;
    @Column(length = 1000)
    private String cInfo;
    private String imageUrl;

    @ManyToOne
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

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
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

    public Contact(int cId, String cName, String cNickName, String cEmail, String cWork, String cPhone, String cInfo,
            String imageUrl, User user) {
        this.cId = cId;
        this.cName = cName;
        this.cNickName = cNickName;
        this.cEmail = cEmail;
        this.cWork = cWork;
        this.cPhone = cPhone;
        this.cInfo = cInfo;
        this.imageUrl = imageUrl;
        this.user = user;
    }

}
