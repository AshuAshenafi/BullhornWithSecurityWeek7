package com.example.demo;

import com.example.demo.Model.News.Interest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users_db")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message="Must give Username!")
    @Size(min=2, max=50)
    @Column(name = "username")
    private String username;

    @NotBlank(message="Must give Email Address!")
    @Size(min=3, max=50)
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank(message="Password must be atleast three Characters!")
    @Size(min=3)
    @Column(name = "password")
    private String password;

    @NotBlank(message="Must give First Name!")
    @Size(min=2, max=50)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message="Must give Last Name!")
    @Size(min=2, max=50)
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "enabled")
    private boolean enabled;

//    private String confirmPassword;

    @ManyToMany
    private Set<Interest> interests = new HashSet<>();

    public User() {
    }

    public User(String username, String email, String password,
                String firstName, String lastName, boolean enabled) {
        this.username = username;
        this.email = email;
        this.setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;

    }

    public void clearPassword(){
        this.password = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password.length() < 3){
            this.password = password;
        } else{
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

//    public String getConfirmPassword() {
//        return confirmPassword;
//    }
//
//    public void setConfirmPassword(String confirmPassword) {
//        this.confirmPassword = confirmPassword;
//    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }
}
