package com.smart.smartcontactmanager.dao;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Query("from Contact as c where c.user.userId =:userId")
    // currentpage - page
    // Contact Per page - 5
    public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.cNickName LIKE %:str% AND c.user =:user")
    // search
    public List<Contact> findByCNicknameContainingAndUser(String str, User user);
}
