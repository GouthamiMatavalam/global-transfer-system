package com.assignment.globaltransfersystem.repository;

import com.assignment.globaltransfersystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AccountRepository for database queries
 *
 * @author Gouthami Matavalam
 *
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByClientId(Long id);
}
