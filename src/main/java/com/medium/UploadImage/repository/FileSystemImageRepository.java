package com.medium.UploadImage.repository;

import com.medium.UploadImage.model.FileSystemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileSystemImageRepository extends JpaRepository<FileSystemImage,Long> {
    FileSystemImage findByName(String name);
}
