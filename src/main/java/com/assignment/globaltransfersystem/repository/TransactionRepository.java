package com.assignment.globaltransfersystem.repository;

import com.assignment.globaltransfersystem.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TransactionRepository for database queries
 *
 * @author Gouthami Matavalam
 *
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    List<Transactions> findByAccountIdOrderByCreatedDateDesc(Long id);
}
