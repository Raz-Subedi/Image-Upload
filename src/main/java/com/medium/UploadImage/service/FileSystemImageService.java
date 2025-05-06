package com.medium.UploadImage.service;

import com.medium.UploadImage.model.FileSystemImage;
import com.medium.UploadImage.repository.FileSystemImageRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemImageService {

    @Value("${image.upload-dir}")
    private String uploadDir;

    private final FileSystemImageRepository fileSystemImageRepository;

    public FileSystemImageService(FileSystemImageRepository fileSystemImageRepository){
        this.fileSystemImageRepository = fileSystemImageRepository;
    }

    public FileSystemImage saveImage(MultipartFile file) throws IOException{
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir,fileName);

        // Create upload directory if it doesn't exist
        Files.createDirectories(filePath.getParent());

        // Save file to disk
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Save metadata to DB
        FileSystemImage image = new FileSystemImage();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setFilePath(filePath.toString());

        return fileSystemImageRepository.save(image);
    }


    public byte[] getImageData(String name) throws IOException {
        FileSystemImage image = fileSystemImageRepository.findByName(name);
        if (image == null) return null;
        Path path = Paths.get(image.getFilePath());
        return Files.readAllBytes(path);
    }

    public String getImageType(String name) {
        FileSystemImage image = fileSystemImageRepository.findByName(name);
        return image != null ? image.getType() : null;
    }

    public Path getImagePath(String name){
        FileSystemImage image = fileSystemImageRepository.findByName(name);
        if (image == null) return null;
        return Paths.get(image.getFilePath());
    }
}
