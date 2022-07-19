package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping
    public String createCredential(Authentication authentication, RedirectAttributes redirectAttributes, CredentialForm credentialForm, Model model){
        redirectAttributes.addFlashAttribute("activeTab", "credentials");
        try{
            Integer result = credentialService.createCredential(credentialForm, authentication.getName());
            if(result > 0){
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "New credential created");
            }else {
                redirectAttributes.addFlashAttribute("success", false);
                redirectAttributes.addFlashAttribute("message", "Error while creating credential.");
            }
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Error while creating credential.");
        }
        return "redirect:/result";
    }

    @GetMapping("/{credentialId}")
    public String deleteCredential(Authentication authentication, RedirectAttributes redirectAttributes, Model model,  @PathVariable("credentialId") Long credentialId){
        credentialService.deleteCredential(credentialId);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Credential deleted");
        return "redirect:/result";
    }
}
