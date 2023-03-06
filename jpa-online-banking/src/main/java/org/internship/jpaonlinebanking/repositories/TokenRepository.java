package org.internship.jpaonlinebanking.repositories;

import org.internship.jpaonlinebanking.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllByIsExpiredOrIsRevokedAndUser_UserId(boolean isExpired, boolean isRevoked, Long userId);
    Optional<Token> findByToken(String token);
}
