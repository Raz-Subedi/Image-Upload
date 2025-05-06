package com.medium.UploadImage.Controller;

import com.medium.UploadImage.service.FileSystemImageService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/file/images")
public class FileSystemImageController {

    private final FileSystemImageService fileSystemImageService;

    public FileSystemImageController(FileSystemImageService fileSystemImageService){
        this.fileSystemImageService = fileSystemImageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            fileSystemImageService.saveImage(file);
            return ResponseEntity.ok("Image uploaded and saved to disk.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Image upload failed.");
        }
    }

    @GetMapping("/view/{name}")
    public ResponseEntity<byte[]> viewImage(@PathVariable String name) {
        try {
            byte[] imageData = fileSystemImageService.getImageData(name);
            if (imageData == null) return ResponseEntity.notFound().build();

            String contentType = fileSystemImageService.getImageType(name);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + name + "\"")
                    .body(imageData);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/stream/{name}")
    public ResponseEntity<Resource> streamImage(@PathVariable String name) throws IOException {
        Path path = fileSystemImageService.getImagePath(name);
        //Path path = Paths.get("uploads").resolve(imagePath);

        if (!Files.exists(path)) return ResponseEntity.notFound().build();

        Resource resource = new FileSystemResource(path);
        String contentType = fileSystemImageService.getImageType(name);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + name + "\"")
                .body(resource);
    }
}
