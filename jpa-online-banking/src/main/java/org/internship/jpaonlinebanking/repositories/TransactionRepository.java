package org.internship.jpaonlinebanking.repositories;

import org.internship.jpaonlinebanking.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionType_Id(Long typeId);
    List<Transaction> findByBaseAccount_AccountId(Long accountId);
}
