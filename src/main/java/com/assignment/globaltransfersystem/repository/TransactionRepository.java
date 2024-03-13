package com.assignment.globaltransfersystem.repository;

import com.assignment.globaltransfersystem.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value="SELECT * FROM gtsdevschema.transactions WHERE account_id=?1 ORDER BY created_date desc offset ?2 limit ?3", nativeQuery = true)
    List<Transactions> getAccountTransactions(Long accountId, int offset, int limit);
}
