package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;

import java.util.Set;

public interface ActorService {

    Set<NoteCommand> findAllNoteByActorId (Long l);
}
