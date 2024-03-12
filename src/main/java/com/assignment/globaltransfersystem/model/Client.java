package com.assignment.globaltransfersystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Client class to map the records in database table CLIENTS
 *
 * @author Gouthami Matavalam
 *
 */

@Entity
@Table(name = "CLIENTS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
