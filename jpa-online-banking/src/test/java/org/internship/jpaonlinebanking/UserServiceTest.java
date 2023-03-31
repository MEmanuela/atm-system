package org.internship.jpaonlinebanking;

import org.internship.jpaonlinebanking.dtos.UserDTO;
import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.mappers.UserMapper;
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
    @Mock
    private UserMapper mapper;
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
        UserDTO actual = underTest.getUserById(1l);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(UserMapper.INSTANCE.toUserDTO(user));
        verify(userRepository).findById(anyLong());
    }
    @Test
    void canCreateUser() {
        //given
        UserDTO dto = new UserDTO();
        dto.setName("John Doe");
        dto.setEmail("john.doe@gmail.com");
        dto.setPhone("0857745896");
        dto.setPersonalCodeNumber("9625418852367");

        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@gmail.com");
        user.setPhone("0857745896");
        user.setPersonalCodeNumber("9625418852367");
        user.setUsername("john_doe");
        user.setPassword("doe188Joh#");
        user.setRole(new Role(1l, "admin"));
        //when
        when(roleRepository.findById(1l))
                .thenReturn(Optional.of(new Role(1l, "admin")));
        underTest.createUser(1l, dto);
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
        UserDTO dto = new UserDTO();
        dto.setName("John Doe");
        dto.setEmail("john.doe@gmail.com");
        dto.setPhone("0857745896");
        dto.setPersonalCodeNumber("9625418852367");

        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@gmail.com");
        user.setPhone("0857745896");
        user.setPersonalCodeNumber("9625418852367");
        user.setUsername("john_doe");
        user.setPassword("doe188Joh#");
        user.setRole(new Role(1l, "admin"));
        //when
        when(roleRepository.findById(1l))
                .thenReturn(Optional.of(new Role(1l, "admin")));
        underTest.createUser(1l, dto);
        //then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getUsername()).isEqualTo("john_doe");
    }
    @Test
    void createdUserHasDefaultPassword() {
        //given
        UserDTO dto = new UserDTO();
        dto.setName("John Doe");
        dto.setEmail("john.doe@gmail.com");
        dto.setPhone("0857745896");
        dto.setPersonalCodeNumber("9625418852367");

        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@gmail.com");
        user.setPhone("0857745896");
        user.setPersonalCodeNumber("9625418852367");
        user.setUsername("john_doe");
        user.setPassword("doe188Joh#");
        user.setRole(new Role(1l, "admin"));
        //when
        when(roleRepository.findById(1l))
                .thenReturn(Optional.of(new Role(1l, "admin")));
        underTest.createUser(1l, dto);
        //then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getPassword()).isEqualTo("doe188Joh#");
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
