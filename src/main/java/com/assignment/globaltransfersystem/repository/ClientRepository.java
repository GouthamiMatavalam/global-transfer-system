package com.assignment.globaltransfersystem.repository;

import com.assignment.globaltransfersystem.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ClientRepository for database queries
 *
 * @author Gouthami Matavalam
 *
 */

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
