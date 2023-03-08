package org.internship.jpaonlinebanking.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.exceptions.ResourceNotFoundException;
import org.internship.jpaonlinebanking.repositories.RoleRepository;
import org.internship.jpaonlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

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
    public Optional<User> getUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        }
        return userRepository.findById(userId);
    }
    public String generateDefaultUsername(String name) {
        int cnt = 0;
        if (userRepository.findByName(name).size() > 0) {
            cnt = userRepository.findByName(name).size();
            return name.toLowerCase().replace(" ", "_").concat(String.valueOf(cnt));
        }
        return name.toLowerCase().replace(" ", "_");
    }
    public String generateDefaultPassword(String n, String pcn) {
        String name = n.toLowerCase().replace(" ", "");
        String password = name.substring(name.length() -3, name.length())
                + pcn.substring(pcn.length()/2-1, pcn.length()/2 + 2)
                + name.substring(0, 3);
        return password;
    }
    @Transactional
    public User createUser(Long roleId, User user) {
        List<User> users = new ArrayList<User>();
        Role role1 = new Role();

        Optional<Role> byId = roleRepository.findById(roleId);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException("Role with id " + roleId + " does not exist");
        }
        Role role = byId.get();

        // tie Role to User
        user.setRole(role);
        String username = generateDefaultUsername(user.getName());
        String password = generateDefaultPassword(user.getName(), user.getPersonalCodeNumber());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        User user1 = userRepository.save(user);

        // tie User to Role
        users.add(user1);
        role1.setUsers(users);

        return user1;
    }
    @Transactional
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
