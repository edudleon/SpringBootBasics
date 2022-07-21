package com.udacity.jwdnd.course1.cloudstorage.dto;

public class NoteForm {

    private Long noteId;
    private String noteTitle;
    private String noteDescription;

    public NoteForm() { }

    public Long getNoteId() {return noteId;}

    public void setNoteId(Long noteId) {this.noteId = noteId;}

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

}
