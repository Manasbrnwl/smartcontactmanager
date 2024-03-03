package com.smart.smartcontactmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.smartcontactmanager.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("Select u from User u where u.userEmail=:email")
    public User getuserByUserName(@Param("email") String email);
}
