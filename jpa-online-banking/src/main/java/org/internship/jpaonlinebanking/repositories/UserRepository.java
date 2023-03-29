package org.internship.jpaonlinebanking.repositories;

import org.internship.jpaonlinebanking.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole_RoleId(Long roleId);
    Optional<User> findByUsername(String username);
    List<User> findByName(String name);
    boolean existsByPersonalCodeNumber(String personalCodeNumber);
    boolean existsByEmail(String email);
    int countByName(String name);
}
