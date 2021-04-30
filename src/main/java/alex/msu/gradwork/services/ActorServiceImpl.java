package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.ActorCommand;
import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.converters.ActorToActorCommand;
import alex.msu.gradwork.converters.NoteToNoteCommand;
import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.repositories.ActorRepository;
import alex.msu.gradwork.repositories.NoteRepository;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;
    private final NoteToNoteCommand noteToNoteCommand;
    private final NoteRepository noteRepository;
    private final ActorToActorCommand actorToActorCommand;

    public ActorServiceImpl(ActorRepository actorRepository, NoteToNoteCommand noteToNoteCommand, NoteRepository noteRepository, ActorToActorCommand actorToActorCommand) {
        this.actorRepository = actorRepository;
        this.noteToNoteCommand = noteToNoteCommand;
        this.noteRepository = noteRepository;
        this.actorToActorCommand = actorToActorCommand;
    }


    //Возвращаем множество дел, принадлежащих данному именному указателю
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

    @Override
    public Actor findById(Long l) {
        return actorRepository.findById(l).get();
    }

    //Возращает постранично отсортированный список именного указателя по Id описи
    @Override
    public Page<Actor> findPaginated(Long registerId, int pageNumber, int pageSize, String sortField, String sortDirection) {

        final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return actorRepository.findActorsByRegisterId(registerId, pageable);
    }

    @Override
    @Transactional
    @Synchronized
    public ActorCommand saveActorCommand(String registerId, String notesText, ActorCommand command) {

//        Optional<Register> registerOptional = registerRepository.findById(Long.valueOf(registerId));
//        Register registerFound = registerOptional.get();
        Optional<Actor> actorOptional = actorRepository.findById(command.getId());
        Actor actorFound = actorOptional.get();

        actorFound.setName(command.getName());
        actorFound.setPatronymic(command.getPatronymic());
        actorFound.setSurname(command.getSurname());
        actorFound.setMemo(command.getMemo());

        //Парсим номера дел, которые надо прикрепить к предметному указателю
        if (!notesText.equals("")) {
            String[] parts = notesText.split("_");
            Set<Long> noteNumbers = new HashSet<>();

            for (String string : parts){
                try {
                    noteNumbers.add(Long.valueOf(string));
                } catch (NumberFormatException e) {
                    continue;
                }
            }

            for (Long l : noteNumbers) {
                //Добавляем предметный указатель к делу, если дело с данным номером существует
                Optional<Note> noteOptional = noteRepository.findNoteByNumber(l);
                noteOptional.ifPresent(note -> note.getActors().add(actorFound));
            }
        }

        return actorToActorCommand.convert(actorFound);
    }
}
