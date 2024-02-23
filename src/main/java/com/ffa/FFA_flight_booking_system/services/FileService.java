package com.ffa.FFA_flight_booking_system.services;

import com.ffa.FFA_flight_booking_system.dto.UserDTO;
import com.ffa.FFA_flight_booking_system.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import com.ffa.FFA_flight_booking_system.models.File;
import com.ffa.FFA_flight_booking_system.repositories.FileRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final UserService userService;

    public FileService(FileRepository fileRepository, UserService userService) {
        this.fileRepository = fileRepository;
        this.userService = userService;
    }

    public ResponseEntity<byte[]> getAllUserFiles() {
        Collection<File> files;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        UserDTO userDTO = userService.getUser(currentUsername);

        System.out.println("ADMIN?: " + userService.hasAdminAuthority(userDTO));

        // If user is admin, return all files, else only return user's own files
        if (userService.hasAdminAuthority(userDTO)) {
            files = fileRepository.findAll();
        } else {
            files = fileRepository.findAllByUser(userService.toUser(userDTO));
        }

        if (!files.isEmpty()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                for (File file : files) {
                    ZipEntry entry = new ZipEntry(file.getFileName());
                    zos.putNextEntry(entry);
                    zos.write(file.getDocFile());
                    zos.closeEntry();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error downloading all files");
            }

            byte[] zipContent = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "files.zip");

            return new ResponseEntity<>(zipContent, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public File getFile(String fileName) {
        return fileRepository.findByFileName(fileName);
    }

    public void uploadFile(MultipartFile file) throws IOException {
        String name = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        UserDTO userDTO = userService.getUser(currentUsername);

        File newFile = new File();
        newFile.setFileName(name);
        newFile.setUser(userService.toUser(userDTO));
        newFile.setDocFile(file.getBytes());

        fileRepository.save(newFile);
    }

    public ResponseEntity<Object> singleFileDownload(String fileName, HttpServletRequest request){
        File document = fileRepository.findByFileName(fileName);

        if (document == null || document.getDocFile() == null) {
            // Handle the case when the document is not found or the content is null
            return ResponseEntity.notFound().build();
        }

        try (InputStream inputStream = new ByteArrayInputStream(document.getDocFile())) {
            // Determine the MIME type dynamically
            String mimeType = request.getServletContext().getMimeType(fileName);

            // If the MIME type is not determined, set the default value
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            // Build the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));
            headers.setContentDispositionFormData("inline", fileName); // Use "inline" for displaying content in the browser

            // Create the InputStreamResource
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

            // Return the ResponseEntity with InputStreamResource
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(inputStreamResource);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while creating the response", e);
        }
    }

}
