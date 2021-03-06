package com.example.application.data.service;

import com.example.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User getByLogin(String login);

    @Query("SELECT u from User u where u.id IN (SELECT userid from UserHW where hwid=:hwId)")
    List<User> getHardwareOwner(@Param("hwId") String serial_hw);

    @Transactional
    @Modifying
    @Query("delete from UserHW where userid=:userId")
    void deleteBindingTableData(@Param("userId") Integer userId);

    @Query("SELECT COUNT(id) FROM UserHW WHERE userid = :userId")
    int checkIfUserOwnsHW(@Param("userId") Integer userId);

    User findByEmail(String email);
    User findByLogin(String login);
}