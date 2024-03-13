package com.assignment.globaltransfersystem.repository;

import com.assignment.globaltransfersystem.model.Transactions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TransactionRepository for database queries
 *
 * @author Gouthami Matavalam
 *
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long>, PagingAndSortingRepository<Transactions, Long> {

    List<Transactions> findByAccountId(Long accountId, Pageable pageable);
}
