package org.example.miniprojectss05.service;

import jakarta.annotation.PostConstruct;
import org.example.miniprojectss05.exception.BadRequestException;
import org.example.miniprojectss05.exception.FileStorageException;
import org.example.miniprojectss05.exception.UnsupportedMediaTypeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final List<String> ALLOWED_EXT = Arrays.asList("jpg", "jpeg", "png");
    private static final long MAX_SIZE = 5L * 1024 * 1024;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path uploadPath;

    @PostConstruct
    public void init() {
        try {
            uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new FileStorageException("Khong tao duoc thu muc uploads", e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File khong duoc rong");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new BadRequestException("File vuot qua 5MB");
        }
        String original = file.getOriginalFilename();
        if (original == null || !original.contains(".")) {
            throw new UnsupportedMediaTypeException("File khong co dinh dang hop le");
        }
        String ext = original.substring(original.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_EXT.contains(ext)) {
            throw new UnsupportedMediaTypeException("Chi chap nhan jpg, jpeg, png");
        }

        String newName = UUID.randomUUID() + "." + ext;
        try {
            Path target = uploadPath.resolve(newName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + newName;
        } catch (IOException e) {
            throw new FileStorageException("Loi luu file: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }
        try {
            String filename = imageUrl.replaceFirst("^/uploads/", "");
            Path target = uploadPath.resolve(filename);
            Files.deleteIfExists(target);
        } catch (IOException e) {
            throw new FileStorageException("Loi xoa file: " + e.getMessage(), e);
        }
    }
}
