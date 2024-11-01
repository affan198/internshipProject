package com.uma.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IFileStorageService implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String storeFile(MultipartFile file) {
        try {
            Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(fileStorageLocation);

            String fileName = file.getOriginalFilename();
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }
}



