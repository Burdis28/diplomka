package com.example.application.data.entity;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;
    @Column(name="fullname")
    private String fullName;
    @Column(name="firstname")
    private String firstName;
    private String login, surname, phone, email;
    @Column(name="passwordhash")
    private String passwordHash;
    @Column(name="pushtoken")
    private String pushToken;
    @Column(name="isadmin")
    private boolean isAdmin;
    @Column(name="isactive")
    private boolean isActive;

    public User() {
    }

    public User(String login, String fullName, String firstName, String surname, String phone, String email, String password) {
        this.login = login;
        this.fullName = fullName;
        this.firstName = firstName;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.passwordHash = DigestUtils.md5Hex(password);
        this.pushToken = null;
        this.isAdmin = false;
        this.isActive = true;
    }

    public boolean checkPassword(String password) {
        return DigestUtils.md5Hex(password).equals(this.passwordHash);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getId() {
        return id;
    }
}
