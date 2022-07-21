package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public int createFile(MultipartFile multipartFile, String username) throws IOException {
        File file = new File(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(), String.valueOf(multipartFile.getSize()), userMapper.getUser(username).getUserId(), multipartFile.getBytes());
        return fileMapper.insert(file);
    }

    public List<File> getFilesByUser(String username){
        return fileMapper.findFilesByUserId(userMapper.getUser(username).getUserId());
    }

    public void deleteFile(Long fileId){
        fileMapper.delete(fileId);
    }

    public File getFileById(Long fileId){
        return fileMapper.findFileById(fileId);
    }
}
