package com.example.application.data.service;

import com.example.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    User getByLogin(String login);

    @Query("SELECT u from User u where u.id = (SELECT userid from UserHW where hwid=:hwId)")
    User getHardwareOwner(@Param("hwId") String serial_hw);
}
