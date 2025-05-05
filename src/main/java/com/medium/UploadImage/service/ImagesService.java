package com.medium.UploadImage.service;

import com.medium.UploadImage.model.Images;
import com.medium.UploadImage.repository.ImagesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImagesService {

    private final ImagesRepository imagesRepository;

    public ImagesService(ImagesRepository imagesRepository){
        this.imagesRepository = imagesRepository;
    }

    public void saveImage(Images images){
        imagesRepository.save(images);
    }

    @Transactional
    public Images getImage(String name){
        return imagesRepository.findByName(name);
    }
}
