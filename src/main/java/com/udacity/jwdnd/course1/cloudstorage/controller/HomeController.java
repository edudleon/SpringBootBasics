package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.dto.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final NoteService noteService;
    private final FileService fileService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(NoteService noteService, FileService fileService, CredentialService credentialService, EncryptionService encryptionService) {
        this.noteService=noteService;
        this.fileService=fileService;
        this.credentialService=credentialService;
        this.encryptionService=encryptionService;
    }

    @ModelAttribute
    public NoteForm getNoteForm(){
        return new NoteForm();
    }

    @ModelAttribute
    public CredentialForm getCredentialForm(){
        return new CredentialForm();
    }

    @ModelAttribute("fileDTO")
    public FileDTO getFileDTO(){return new FileDTO();}

    @GetMapping()
    public String getHomePage(Authentication authentication, Model model) {
        model.addAttribute("userNotes", this.noteService.getNotesByUser(authentication.getName()));
        model.addAttribute("userFiles", this.fileService.getFilesByUser(authentication.getName()));
        model.addAttribute("userCredentials", this.credentialService.getCredentialsByUser(authentication.getName()));
        model.addAttribute("encryptionService", this.encryptionService);
        return "home";
    }

}
