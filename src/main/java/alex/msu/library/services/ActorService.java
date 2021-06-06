package alex.msu.library.services;

import alex.msu.library.commands.ActorCommand;
import alex.msu.library.commands.NoteCommand;
import alex.msu.library.domain.Actor;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface ActorService {

    Set<NoteCommand> findAllNoteByActorId (Long l);

    //Возращает постранично отсортированный список именного указателя по Id описи

    Actor findById(Long l);

    //Возращает постранично отсортированный список именного указателя по Id описи
    Page<Actor> findPaginated(Long registerId, int pageNumber, int pageSize, String sortField, String sortDirection);

    ActorCommand saveActorCommand(String registerId, String notesText, ActorCommand command);

    void DeleteById(Long registerId, Long idToDelete);

    void DeleteRelationWithNote(Long registerId, Long noteId, Long actorId);
}
