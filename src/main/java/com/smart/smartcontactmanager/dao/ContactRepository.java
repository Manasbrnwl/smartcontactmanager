package com.smart.smartcontactmanager.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.smart.smartcontactmanager.entities.Contact;

public interface ContactRepository extends CrudRepository<Contact, Integer> {

    @Query("from Contact as c where c.user.userId =:userId")
    // currentpage - page
    // Contact Per page - 5
    public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);
}
