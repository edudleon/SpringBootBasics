package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.dto.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    public int createNote(NoteForm note, String userName){
        return noteMapper.insert(new Note(null, note.getNoteTitle(), note.getNoteDescription(), userMapper.getUser(userName).getUserId()));
    }

    public int updateNote(NoteForm note){
       Note originalNote = noteMapper.findNoteById(note.getNoteId());
       originalNote.setNoteTitle(note.getNoteTitle());
       originalNote.setNoteDescription(note.getNoteDescription());
       return noteMapper.update(originalNote);
    }

    public List<Note> getNotesByUser(String userName){
        return noteMapper.findNotesByUserId(userMapper.getUser(userName).getUserId());
    }

    public void deleteNote(Long noteId){
        noteMapper.delete(noteId);
    }
}
