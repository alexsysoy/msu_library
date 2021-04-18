package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Note;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface NoteService {

    Long getTotalNotes();

    Page<Note> findPaginated(int pageNumber, int pageSize, String sortField, String sortDirection);

    Set<Note> getNotes();

    Note findById(Long l);

    NoteCommand findByRegisterIdAndNoteId(Long registerId, Long noteId);

    Set<Note>  findNoteCommand(NoteCommand noteCommand);

    NoteCommand saveNoteCommand(NoteCommand command);

    void DeleteById(Long registerId, Long idToDelete);

}
