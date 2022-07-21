package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/credentials")
public class CredentialController {

    CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping
    public String createOrUpdateCredential(Authentication authentication, RedirectAttributes redirectAttributes, CredentialForm credentialForm, Model model) {
        redirectAttributes.addFlashAttribute("activeTab", "credentials");
        if (credentialForm.getCredentialId() == null) {
            try {
                Integer result = credentialService.createCredential(credentialForm, authentication.getName());
                if (result > 0) {
                    redirectAttributes.addFlashAttribute("success", true);
                    redirectAttributes.addFlashAttribute("message", "New credential created");
                } else {
                    redirectAttributes.addFlashAttribute("success", false);
                    redirectAttributes.addFlashAttribute("message", "Error while creating credential.");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("success", false);
                redirectAttributes.addFlashAttribute("message", "Error while creating credential.");
            }
        } else {
            try {
                Integer result = credentialService.updateCredential(credentialForm);
                if (result > 0) {
                    redirectAttributes.addFlashAttribute("success", true);
                    redirectAttributes.addFlashAttribute("message", "Credential successfully updated");
                } else {
                    redirectAttributes.addFlashAttribute("success", false);
                    redirectAttributes.addFlashAttribute("message", "Error while updating credential.");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("success", false);
                redirectAttributes.addFlashAttribute("message", "Error while updating credential.");
            }
        }

        return "redirect:/home/result";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(Authentication authentication, RedirectAttributes redirectAttributes, Model model, @PathVariable("credentialId") Long credentialId) {
        credentialService.deleteCredential(credentialId);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Credential deleted");
        return "redirect:/home/result";
    }
}
