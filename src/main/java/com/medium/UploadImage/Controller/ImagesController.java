package com.medium.UploadImage.Controller;


import com.medium.UploadImage.model.Images;
import com.medium.UploadImage.service.ImagesService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/base64/images")
public class ImagesController {

    private final ImagesService imagesService;

    public ImagesController(ImagesService imagesService){
        this.imagesService = imagesService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());

            Images image = new Images();
            image.setName(file.getOriginalFilename());
            image.setType(file.getContentType());
            image.setBase64Data(base64);

            imagesService.saveImage(image);

            return ResponseEntity.ok("Image uploaded and stored successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to process image.");
        }
    }


    @GetMapping("/{name}")
    public ResponseEntity<Images> getImage(@PathVariable String name) {
        Images images = imagesService.getImage(name);
        if (images == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(images);
    }

    @GetMapping("/view/{name}")
    public ResponseEntity<byte[]> viewImage(@PathVariable String name) {
        Images image = imagesService.getImage(name);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Base64.getDecoder().decode(image.getBase64Data());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getType()))
                //.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getName() + "\"")
                .body(imageBytes);
    }
}
