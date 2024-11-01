package com.uma.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String type;
    private String location;
    private Double amount;
    private String fileName;  // original file name.
    private String filePath; // Field to store the file path.
    @ManyToOne
    private User user;
    @ManyToOne
    private Admin admin;

    

    // Getters and Setters
}
