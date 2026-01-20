package com.tcs.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class KycRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String panNumber;

    private String documentType;

    private String documentUrl;

    private String status; // PENDING / VERIFIED / REJECTED

    private LocalDateTime submittedAt;

    private LocalDateTime verifiedAt;

    private Long verifiedBy; // adminId

    private String remarks;
}
