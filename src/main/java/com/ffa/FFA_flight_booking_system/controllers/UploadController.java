package com.ffa.FFA_flight_booking_system.controllers;

import com.ffa.FFA_flight_booking_system.models.File;
import com.ffa.FFA_flight_booking_system.services.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/file")
public class UploadController {
    private final FileService fileService;

    public UploadController(FileService fileService) {
        this.fileService = fileService;
    }

    // GET MAPPING, GET DATA

    @GetMapping("/all")
    public ResponseEntity<byte[]> getAllFiles() {
        return fileService.getAllUserFiles();
    }

    @GetMapping("/get/{fileName}")
    public ResponseEntity<byte[]> getFileByName(@PathVariable String fileName) {
        File file = fileService.getFile(fileName);

        if (file != null) {
            return ResponseEntity.ok(file.getDocFile());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST MAPPING, POST DATA

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }

        try {
            fileService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }

    @PostMapping("/upload/multiple")
    public ResponseEntity<String> handleFileUpload(@RequestParam("files") List<MultipartFile> files) {
        if (files.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select at least one file to upload.");
        }

        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    fileService.uploadFile(file);
                }
            }
            return ResponseEntity.ok("Files uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload files: " + e.getMessage());
        }
    }

    @PostMapping("/download/{fileName}")
    public ResponseEntity<Object> handleFileDownload(@PathVariable String fileName, HttpServletRequest request) {
        return fileService.singleFileDownload(fileName, request);
    }
}
