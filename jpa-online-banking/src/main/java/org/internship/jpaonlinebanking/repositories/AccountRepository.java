package org.internship.jpaonlinebanking.repositories;

import org.internship.jpaonlinebanking.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByAccountType_Id(Long typeId);
    List<Account> findByUser_UserId(Long userId);
}
