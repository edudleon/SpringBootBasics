package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NoteController {

    NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public String createNote(Authentication authentication, RedirectAttributes redirectAttributes, NoteForm noteForm, Model model){
        redirectAttributes.addFlashAttribute("activeTab", "notes");
        try{
            Integer result = noteService.createNote(noteForm, authentication.getName());
            if(result > 0){
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "New note created");
            }else{
                redirectAttributes.addFlashAttribute("success", false);
                redirectAttributes.addFlashAttribute("message", "Error while creating note.");
            }
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Error while creating note.");
        }
        return "redirect:/result";
    }

    @GetMapping("/{noteId}")
    public String deleteNote(Authentication authentication, RedirectAttributes redirectAttributes, Model model, @PathVariable("noteId") Long noteId){
        noteService.deleteNote(noteId);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Note deleted");
        return "redirect:/result";
    }
}
