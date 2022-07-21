package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/files")
public class FileController {

    FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @ModelAttribute("fileDTO")
    public FileDTO getFileDTO(){return new FileDTO();}

    @PostMapping
    public String addFile(Authentication authentication, @ModelAttribute("file") MultipartFile file, RedirectAttributes redirectAttributes, Model model) {
        try{
            Integer result = fileService.createFile(file, authentication.getName());
            if(result > 0){
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "New file uploaded");
            }else {
                redirectAttributes.addFlashAttribute("success", false);
                redirectAttributes.addFlashAttribute("message", "Error while uploading file.");
            }
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Error while uploading file.");
        }
        return "redirect:/result";
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(RedirectAttributes redirectAttributes, @PathVariable("fileId") Long fileId){
        fileService.deleteFile(fileId);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "File deleted");
        return "redirect:/result";
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> viewFile(@PathVariable("fileId") Long fileId){
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType( file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

}
