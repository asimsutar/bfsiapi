package com.tcs.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tcs.dto.KycSubmitRequest;
import com.tcs.dto.UserResponse;
import com.tcs.entity.KycRequest;
import com.tcs.exception.KycException;
import com.tcs.repository.KycRepository;
import com.tcs.storing.FileStorageService;
import com.tcs.util.JwtUtil;

@RestController
@RequestMapping("/kyc")
public class KycController {
    @Autowired
    private KycRepository kycRepository;
    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @PostMapping(
            value = "/submit",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> submitKyc(
            @RequestParam String panNumber,
            @RequestParam String documentType,
            @RequestPart MultipartFile document,
            @RequestHeader("Authorization") String token) {

        // Extract username from JWT
        String username = jwtUtil.extractUsername(token);

        //  Call USER-SERVICE
        String url = userServiceUrl + "/users/usernamekyc/" + username;
        UserResponse user = restTemplate.getForObject(url, UserResponse.class);

        if (user == null) {
            throw new KycException("User not found");
        }
        if (panNumber == null || panNumber.isBlank()) {
            throw new KycException("PAN number is required");
        }

        // DEBUG LOG (KEEP THIS FOR NOW)
        System.out.println("Username from JWT = " + username);
        System.out.println("PAN from request = [" + panNumber + "]");
        System.out.println("PAN from DB      = [" + user.getPan() + "]");

        // PAN NORMALIZATION (CRITICAL FIX)
        String requestPan = panNumber.trim().toUpperCase();
        String storedPan  = user.getPan().trim().toUpperCase();

        if (!requestPan.equals(storedPan)) {
            throw new KycException("PAN does not match registered PAN");
        }

        //  Prevent duplicate KYC
        if (kycRepository.existsByUserIdAndStatusIn(
                user.getId(), List.of("PENDING", "VERIFIED"))) {
            throw new KycException("KYC already submitted");
        }

        //  Save file locally
        String storedPath = fileStorageService.save(document);

        //  Save KYC record
        KycRequest kyc = new KycRequest();
        kyc.setUserId(user.getId());
        kyc.setPanNumber(requestPan);
        kyc.setDocumentType(documentType);
        kyc.setDocumentUrl(storedPath);
        kyc.setStatus("PENDING");
        kyc.setSubmittedAt(LocalDateTime.now());

        kycRepository.save(kyc);

        return ResponseEntity.ok("KYC submitted successfully");
    }

    
    @GetMapping("/document/{kycId}")
    public ResponseEntity<Resource> viewDocument(
            @PathVariable Long kycId) {

        KycRequest kyc = kycRepository.findById(kycId)
            .orElseThrow(() -> new KycException("KYC not found"));

        Resource file =
            fileStorageService.load(kyc.getDocumentUrl());

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"kyc-document\""
            )
            .body(file);
    }
}
