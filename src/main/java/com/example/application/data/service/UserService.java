package com.example.application.data.service;

import com.example.application.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;

@Service
public class UserService extends CrudService<User, Integer> {

    private final UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected JpaRepository<User, Integer> getRepository() {
        return userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> getHardwareOwner(String serialHW) {
        return userRepository.getHardwareOwner(serialHW);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteBindingTableData(id);
        super.delete(id);
    }

    public boolean userOwnsAnyHW(Integer userId) {
        return userRepository.checkIfUserOwnsHW(userId) > 0;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
