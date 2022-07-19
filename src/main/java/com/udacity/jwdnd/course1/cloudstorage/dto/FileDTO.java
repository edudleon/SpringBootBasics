package com.udacity.jwdnd.course1.cloudstorage.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileDTO {
    private MultipartFile file;

    public FileDTO(MultipartFile file) {
        this.file = file;
    }

    public FileDTO() {  }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
