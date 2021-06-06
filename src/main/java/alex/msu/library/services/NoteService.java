package alex.msu.library.services;

import alex.msu.library.commands.NoteCommand;
import alex.msu.library.domain.Note;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface NoteService {

    Page<Note> findPaginated(Long registerId, int pageNumber, int pageSize, String sortField, String sortDirection);

    Set<Note> getNotes();

    Note findById(Long l);

    Note findByNumber(Long l);

    NoteCommand findByRegisterIdAndNoteId(Long registerId, Long noteId);

    Set<Note>  findNoteCommand(NoteCommand noteCommand);

    NoteCommand saveNoteCommand(NoteCommand command);

    NoteCommand createNoteCommand(NoteCommand command);

    //Поиск Дела по номеру Изображения и номеру Дела
    NoteCommand findByImageIdAndNoteId(Long imageId, Long noteId);

    void DeleteById(Long registerId, Long idToDelete);

    Set<Note> searchNotes(NoteCommand command);
}
