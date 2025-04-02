package com.ChaTop.Backend.Services;




import com.ChaTop.Backend.Config.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;





@Service
public class FileUploadService {




    private final Path fileUploadLocation;




    @Autowired
    public FileUploadService(FileStorageProperties fileStorageProperties) {
        
        
        
        this.fileUploadLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileUploadLocation);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create the directory where you want to the uploaded the files will be kept.", e);
        }



    }




    public String uploadFile(MultipartFile file) {




        // Renormalize the file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Verify if the file's name  is containing invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! File name is containing invalid path sequence " + fileName);
            }
            // Copy file to the target path (replacing existing file with the same name)
            Path targetLocation = this.fileUploadLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }




    }














}