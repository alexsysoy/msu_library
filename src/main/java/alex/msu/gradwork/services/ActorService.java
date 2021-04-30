package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Actor;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface ActorService {

    Set<NoteCommand> findAllNoteByActorId (Long l);

    //Возращает постранично отсортированный список именного указателя по Id описи
    Page<Actor> findPaginated(Long registerId, int pageNumber, int pageSize, String sortField, String sortDirection);
}
