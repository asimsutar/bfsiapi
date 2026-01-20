package com.tcs.storing;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    private static final String BASE_DIR = "/home/labuser/kyc-documents/";

    public String save(MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(BASE_DIR));

            String filename =
                UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path targetPath = Paths.get(BASE_DIR).resolve(filename);

            Files.copy(
                file.getInputStream(),
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
            );

            return targetPath.toString(); // stored in DB

        } catch (IOException e) {
            throw new RuntimeException("File storage failed", e);
        }
    }

    public Resource load(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }

            return resource;
        } catch (Exception e) {
            throw new RuntimeException("File load failed", e);
        }
    }
}
