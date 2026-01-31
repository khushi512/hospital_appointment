package com.example.hospital.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    private static final String UPLOAD_DIR = "uploads/appointments/";

    public static String saveFile(MultipartFile file) {

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String filePath = UPLOAD_DIR + System.currentTimeMillis()
                    + "_" + file.getOriginalFilename();

            Path path = Paths.get(filePath);
            Files.write(path, file.getBytes());

            return filePath;

        } catch (IOException e) {
            throw new RuntimeException("File upload failed");
        }
    }
}