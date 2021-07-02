package com.example.application.data.service;

import com.example.application.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.artur.helpers.CrudService;

public class UserService extends CrudService<User, Integer> {

    private UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected JpaRepository<User, Integer> getRepository() {
        return userRepository;
    }
}
