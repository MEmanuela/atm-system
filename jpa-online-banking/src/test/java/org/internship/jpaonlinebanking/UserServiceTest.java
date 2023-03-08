package org.internship.jpaonlinebanking;

import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.repositories.RoleRepository;
import org.internship.jpaonlinebanking.repositories.UserRepository;
import org.internship.jpaonlinebanking.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Bean
    private PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    private UserService underTest;
    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, roleRepository, passwordEncoder());
    }
    @Test
    void canGetAllUsers() {
        //when
        underTest.getAllUsers();
        //then
        verify(userRepository).findAll();
    }
    @Test
    void canGetUserById() {
        //given
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@gmail.com");
        user.setPhone("0857745896");
        user.setPersonalCodeNumber("9625418852367");
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        var actual = underTest.getUserById(1l);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(Optional.of(user));
        verify(userRepository).findById(anyLong());
    }
    @Test
    void canCreateUser() {
        //given
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@gmail.com");
        user.setPhone("0857745896");
        user.setPersonalCodeNumber("9625418852367");
        //when
        when(roleRepository.findById(1l))
                .thenReturn(Optional.of(new Role(1l, "admin")));
        underTest.createUser(1l, user);
        //then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);

    }
    @Test
    void createdUserHasDefaultUsername() {
        //given
        User user = new User();
        user.setName("John Doe");
        user.setPersonalCodeNumber("9625418852367");
        //when
        when(roleRepository.findById(1l))
                .thenReturn(Optional.of(new Role(1l, "admin")));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        underTest.createUser(1l, user);
        //then
        assertThat(userRepository.findById(anyLong()).get()
                .getUsername()).isEqualTo("john_doe");

    }
    @Test
    void createdUserHasDefaultPassword() {
        //given
        User user = new User();
        user.setName("John Doe");
        user.setPersonalCodeNumber("9625418852367");
        //when
        when(roleRepository.findById(1l))
                .thenReturn(Optional.of(new Role(1l, "admin")));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        underTest.createUser(1l, user);
        //then
        assertThat(userRepository.findById(anyLong()).get()
                .getPassword()).isEqualTo("doe188joh");

    }
    @Test
    void canUpdateUserPassword() {
        //given
        User user = new User();
        user.setPassword("password");
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        underTest.updateUserPassword(anyLong(), "newPassword");
        //then
        assertThat(userRepository.findById(anyLong()).get()
                .getPassword()).isEqualTo("newPassword");
    }
    @Test
    void canDeleteUserById() {
        doNothing().when(userRepository).deleteById(anyLong());

        underTest.deleteUserById(anyLong());
        verify(userRepository).deleteById(anyLong());
    }
    @Test
    void canGetUserByRole() {
        //given
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@gmail.com");
        user.setPhone("0857745896");
        user.setPersonalCodeNumber("9625418852367");
        //when
        when(userRepository.findByRole_RoleId(anyLong()))
                .thenReturn(List.of(user));
        var actual = underTest.getUsersByRole(1l);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(user));
        verify(userRepository).findByRole_RoleId(anyLong());
    }
}
