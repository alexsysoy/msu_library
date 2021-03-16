package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Note;

import java.util.Set;

public interface NoteService {

    Set<Note> getNotes();

    Note findById(Long l);

    NoteCommand findByRegisterIdAndNoteId(Long registerId, Long noteId);

    NoteCommand saveNoteCommand(NoteCommand command);

}
