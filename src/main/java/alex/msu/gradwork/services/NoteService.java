package alex.msu.gradwork.services;

import alex.msu.gradwork.domain.Note;

import java.util.Set;

public interface NoteService {

    Set<Note> getNotes();

    Note findById(Long l);

}
