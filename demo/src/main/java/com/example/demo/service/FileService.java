package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    @Value("${demo.upload-file.base-uri}")
    private String baseUri;
    public void createDirectory(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if(!tmpDir.isDirectory()){
            try{
                Files.createDirectories(tmpDir.toPath());
                System.out.println("CREATE NEW DIRECTORY SUCCESS,PATH="+tmpDir.toPath());
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            System.out.println("DIRECTORY ALREADY EXISTS,PATH="+folder);
        }

    }
//    luu file
    public String store(MultipartFile file, String folder) throws URISyntaxException, IOException {
//        create unique fileName
        String finalName = System.currentTimeMillis()+ "-"+ file.getOriginalFilename();
        URI uri = new URI(baseUri+folder+"/"+finalName);
        Path path = Paths.get(uri);
        try(InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);
        }
        return  finalName;
    }

    public long getFileLength(String folder, String fileName) throws URISyntaxException {
        URI uri = new URI(baseUri+folder+"/"+fileName);
        Path path = Paths.get(uri);
        File impFiles = new File(path.toString());
//        file không tồn tại hoặc file là 1 directory
        if(!impFiles.exists() || impFiles.isDirectory()){
            return 0;
        }
        return impFiles.length();
    }
//    downfile
    public InputStreamResource getResource(String folder, String fileName) throws URISyntaxException, FileNotFoundException{
        URI uri = new URI(baseUri+folder+"/"+fileName);
        Path path = Paths.get(uri);
        File tmpFile = new File(path.toString());
        return new InputStreamResource((new FileInputStream(tmpFile)));
    }
}
