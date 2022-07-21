package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/notes")
public class NoteController {

    NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public String createOrUpdateNote(Authentication authentication, RedirectAttributes redirectAttributes, NoteForm noteForm, Model model) {
        redirectAttributes.addFlashAttribute("activeTab", "notes");
        if (noteForm.getNoteId() == null) {
            try {
                Integer result = noteService.createNote(noteForm, authentication.getName());
                if (result > 0) {
                    redirectAttributes.addFlashAttribute("success", true);
                    redirectAttributes.addFlashAttribute("message", "New note created");
                } else {
                    redirectAttributes.addFlashAttribute("success", false);
                    redirectAttributes.addFlashAttribute("message", "Error while creating note.");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("success", false);
                redirectAttributes.addFlashAttribute("message", "Error while creating note.");
            }
        } else {
            try {
                Integer result = noteService.updateNote(noteForm);
                if (result > 0) {
                    redirectAttributes.addFlashAttribute("success", true);
                    redirectAttributes.addFlashAttribute("message", "Note successfully updated");
                } else {
                    redirectAttributes.addFlashAttribute("success", false);
                    redirectAttributes.addFlashAttribute("message", "Error while updating note.");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("success", false);
                redirectAttributes.addFlashAttribute("message", "Error while updating note.");
            }
        }

        return "redirect:/home/result";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(Authentication authentication, RedirectAttributes redirectAttributes, Model model, @PathVariable("noteId") Long noteId) {
        noteService.deleteNote(noteId);
        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "Note deleted");
        return "redirect:/home/result";
    }
}
