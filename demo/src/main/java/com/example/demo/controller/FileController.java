package com.example.demo.controller;



import com.example.demo.dto.response.file.ResUploadFileDTO;
import com.example.demo.service.FileService;
import com.example.demo.util.annotation.ApiMessage;
import com.example.demo.util.error.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    private final FileService fileService;
    @Value("${demo.upload-file.base-uri}")
    private String baseUri;
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> uploadFile(@RequestParam(name = "file",required = false) MultipartFile file,
                                                       @RequestParam("folder") String folder) throws URISyntaxException, IOException,StorageException {
//check valid
        if(file== null ||file.isEmpty()){
            throw  new StorageException("File is empty");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedFileTypes = Arrays.asList("png","jpg","jpeg","doc","pdf","docx");
        boolean isValid = allowedFileTypes.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if(!isValid){
            throw  new StorageException("InValid File extentions. only allow"+allowedFileTypes.toString());
        }
//        create a directory
        this.fileService.createDirectory(baseUri+folder);
//        store file
      String uploadFile= this.fileService.store(file,folder);
        ResUploadFileDTO resUploadFileDTO = new ResUploadFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok(resUploadFileDTO);
    }

    @GetMapping("/files")
    @ApiMessage("Download File")
    public ResponseEntity<Resource> download(
            @RequestParam(name = "fileName",required = false) String fileName,
            @RequestParam(name = "folder",required = false) String folder
    ) throws StorageException,URISyntaxException,FileNotFoundException {
        if(fileName == null || folder == null){
            throw new StorageException("fileName or folder is empty");
        }
//        check file exits (and not a directory)
        long fileLengh = this.fileService.getFileLength(folder,fileName);
        if(fileLengh == 0){
            throw new StorageException("File not found");
        }
        InputStreamResource resource = this.fileService.getResource(folder,fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileName+"\"")
                .contentLength(fileLengh)
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

}
