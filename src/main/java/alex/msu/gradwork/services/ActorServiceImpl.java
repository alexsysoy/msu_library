package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.converters.NoteToNoteCommand;
import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.repositories.ActorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;
    private final NoteToNoteCommand noteToNoteCommand;

    public ActorServiceImpl(ActorRepository actorRepository, NoteToNoteCommand noteToNoteCommand) {
        this.actorRepository = actorRepository;
        this.noteToNoteCommand = noteToNoteCommand;
    }


    //Возвращаем множество Дел, принадлежащих данному именному указателю
    @Override
    public Set<NoteCommand> findAllNoteByActorId(Long l) {

        Set<NoteCommand> noteCommands = new HashSet<>();
        Optional<Actor> actorOptional = actorRepository.findById(l);

        if (actorOptional.isPresent()){
            noteCommands = actorOptional.get()
                    .getNotes()
                    .stream()
                    .map(noteToNoteCommand::convert)
                    .collect(Collectors.toSet());
        }

        return noteCommands;
    }
}
