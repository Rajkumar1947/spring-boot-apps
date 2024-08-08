package com.batch.springboot.springbatch.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class FileDownloadHelper {

    public FileInputStream download(final String fileName) throws FileNotFoundException {
        FileInputStream fileInputStream = null;
        // Adjust the file path based on the lastName parameter if needed
        File file = new File(fileName);
        if (file.exists()) {
            // Open the file input stream
            fileInputStream = new FileInputStream(file);
        }
        return fileInputStream;

    }

}
