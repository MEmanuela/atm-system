package org.internship.jpaonlinebanking.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.internship.jpaonlinebanking.dtos.RoleDTO;
import org.internship.jpaonlinebanking.dtos.UserDTO;
import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.exceptions.ResourceNotFoundException;
import org.internship.jpaonlinebanking.exceptions.UniqueConstraintException;
import org.internship.jpaonlinebanking.mappers.RoleMapper;
import org.internship.jpaonlinebanking.mappers.UserMapper;
import org.internship.jpaonlinebanking.repositories.RoleRepository;
import org.internship.jpaonlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MessageSource messageSource;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOList = UserMapper.INSTANCE.toListOfUserDTOs(users);
        return userDTOList;
    }

    public UserDTO getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound.noSuchUser", null, Locale.ENGLISH));
        }
        return UserMapper.INSTANCE.toUserDTO(user.get());
    }

    public String generateDefaultUsername(String name) {
        int cnt = userRepository.countByName(name);
        if (cnt > 0) {
            return name.toLowerCase().replace(" ", "_").concat(String.valueOf(cnt));
        }
        return name.toLowerCase().replace(" ", "_");
    }

    public String generateDefaultPassword(String n, String pcn) {
        String name = n.substring(0, 1).toUpperCase() +
                n.substring(1).toLowerCase().replace(" ", "");
        return name.substring(name.length() -3, name.length())
                + pcn.substring(pcn.length()/2-1, pcn.length()/2 + 2)
                + name.substring(0, 3) + "#";
    }

    @Transactional
    public void createUser(Long roleId, UserDTO userDTO) {
        Optional<Role> byId = roleRepository.findById(roleId);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound.noSuchRole", null, Locale.ENGLISH));
        }
        Role role = byId.get();
        if (userRepository.existsByPersonalCodeNumber(userDTO.getPersonalCodeNumber())) {
            throw new UniqueConstraintException(messageSource.getMessage("exception.uniqueConstraint.pcn", null, Locale.ENGLISH));
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UniqueConstraintException(messageSource.getMessage("exception.uniqueConstraint.email", null, Locale.ENGLISH));
        }
        // tie Role to User
        String username = generateDefaultUsername(userDTO.getName());
        String password = generateDefaultPassword(userDTO.getName(), userDTO.getPersonalCodeNumber());

        User user = UserMapper.INSTANCE.fromUserDTO(userDTO);
        user.setRole(role);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void updateUserPassword(Long userId, String password) {
        User user = userRepository.findById(userId).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getUsersByRole(Long roleId) {
        return userRepository.findByRole_RoleId(roleId);
    }
}
