package org.internship.jpaonlinebanking.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.internship.jpaonlinebanking.dtos.UserDTO;
import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.exceptions.ResourceNotFoundException;
import org.internship.jpaonlinebanking.exceptions.UniqueConstraintException;
import org.internship.jpaonlinebanking.repositories.RoleRepository;
import org.internship.jpaonlinebanking.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        }
        return user.get();
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
    public User createUser(Long roleId, User user) {
//        List<User> users = new ArrayList<User>();
//        Role role1 = new Role();
        
        Optional<Role> byId = roleRepository.findById(roleId);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException("Role with id " + roleId + " does not exist");
        }
        Role role = byId.get();
        if (userRepository.existsByPersonalCodeNumber(user.getPersonalCodeNumber())) {
            throw new UniqueConstraintException("A user with the specified pcn already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UniqueConstraintException("A user with the specified email address exists");
        }
        // tie Role to User
        user.setRole(role);
        String username = generateDefaultUsername(user.getName());
        String password = generateDefaultPassword(user.getName(), user.getPersonalCodeNumber());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        User storedUser = userRepository.save(user);

        // tie User to Role
//        users.add(user1);
        //role1.setUsers(users);

        return storedUser;
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
