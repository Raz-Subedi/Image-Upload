package com.medium.UploadImage.repository;

import com.medium.UploadImage.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepository extends JpaRepository<Images,Long> {

    Images findByName(String name);
}
