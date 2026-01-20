package com.tcs.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "bankusers",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "panNumber"),
        @UniqueConstraint(columnNames = "phone"),
        @UniqueConstraint(columnNames = "email")
    })
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String panNumber;

    private LocalDate dob;

    private String email;

    private String phone;

    private String address;

    private String occupation;

    private String gender;

    private String role; // ROLE_USER

    private String kycStatus; // PENDING

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
