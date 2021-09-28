package com.example.application.data.service;

import com.example.application.data.entity.UserPasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for password resetting logic representation.
 */
@Service
public class UserPasswordResetService extends CrudService<UserPasswordReset, Integer> {

    private final UserPasswordResetRepository userPasswordResetRepository;

    public UserPasswordResetService(UserPasswordResetRepository userPasswordResetRepository) {
        this.userPasswordResetRepository = userPasswordResetRepository;
    }

    @Override
    protected JpaRepository<UserPasswordReset, Integer> getRepository() {
        return userPasswordResetRepository;
    }

    public List<UserPasswordReset> findByEmail(String email, LocalDateTime dateTime) {
        return userPasswordResetRepository.findByEmailAndIsValid(email, dateTime);
    }
}
