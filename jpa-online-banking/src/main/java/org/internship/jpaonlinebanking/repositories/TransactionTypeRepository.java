package org.internship.jpaonlinebanking.repositories;

import org.internship.jpaonlinebanking.entities.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
}
