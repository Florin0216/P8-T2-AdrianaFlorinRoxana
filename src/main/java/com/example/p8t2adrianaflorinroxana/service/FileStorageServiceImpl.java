package com.example.p8t2adrianaflorinroxana.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImpl {

    public static final String STORAGE_DIRECTORY = "src/main/resources/static/uploads/";

    public void saveFile(MultipartFile fileToSave) throws IOException {
        var targetFile = new File(STORAGE_DIRECTORY + File.separator + fileToSave.getOriginalFilename());
        Files.copy(fileToSave.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public File getDownloadFile(String fileName) throws Exception {
        return new File(STORAGE_DIRECTORY + File.separator + fileName);
    }
}
