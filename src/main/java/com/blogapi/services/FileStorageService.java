package com.blogapi.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {

        try {

            Path uploadPath = Paths.get(uploadDir);

            // Create directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique file name
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Copy file to target location
            Files.copy(file.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), ex);
        }
    }


    public void deleteFile(String fileName){
        try{

            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(filePath);

        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file " + fileName, ex);
        }
    }


    public Path loadFile(String fileName){
        return Paths.get(uploadDir).resolve(fileName);
    }


}
