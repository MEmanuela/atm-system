package org.internship.jpaonlinebanking;

import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    void createdUserHasDefaultUsername() {
        User user = userService.createUser(Long.valueOf(2), User.builder()
                .userId(Long.valueOf(3)).name("John Doe")
                .phone("0856623956").email("john.doe@gmail.com")
                .personalCodeNumber("9625418852367").role(new Role(Long.valueOf(2), "customer"))
                .accounts(new ArrayList<>()).build());
        assertThat(user.getUsername().equals("john_doe"));
    }
    @Test
    void createdUserHasDefaultPassword() {
        User user = userService.createUser(Long.valueOf(2), User.builder()
                .userId(Long.valueOf(3)).name("John Doe")
                .phone("0856623956").email("john.doe@gmail.com")
                .personalCodeNumber("9625418852367").role(new Role(Long.valueOf(2), "customer"))
                .accounts(new ArrayList<>()).build());
        assertThat(user.getPassword().equals(passwordEncoder.encode("doe188joh")));
    }
}
