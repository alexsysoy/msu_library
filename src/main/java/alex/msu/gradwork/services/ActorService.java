package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.ActorCommand;
import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Actor;
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
