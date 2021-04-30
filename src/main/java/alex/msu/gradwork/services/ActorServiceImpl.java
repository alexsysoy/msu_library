package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.ActorCommand;
import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.converters.ActorToActorCommand;
import alex.msu.gradwork.converters.NoteToNoteCommand;
import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.repositories.ActorRepository;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
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
    private final RegisterRepository registerRepository;

    public ActorServiceImpl(ActorRepository actorRepository, NoteToNoteCommand noteToNoteCommand, NoteRepository noteRepository, ActorToActorCommand actorToActorCommand, RegisterRepository registerRepository) {
        this.actorRepository = actorRepository;
        this.noteToNoteCommand = noteToNoteCommand;
        this.noteRepository = noteRepository;
        this.actorToActorCommand = actorToActorCommand;
        this.registerRepository = registerRepository;
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

    @Override
    public void DeleteById(Long registerId, Long idToDelete) {
        log.debug("Удаляем именной указатель id {} и id Описи {}", idToDelete, registerId);

        //Ищем Опись с данным id
        Optional<Register> registerOptional = registerRepository.findById(registerId);
        if(registerOptional.isPresent()){
            Register register = registerOptional.get();

            //Удаляем из описи
            Optional<Actor> actorOptional = register.getActors().stream()
                    .filter(actor -> actor.getId().equals(idToDelete))
                    .findFirst();

            if(actorOptional.isPresent()){
                Actor actorToDelete = actorOptional.get();
                //Уничтожаем ссылки предмета из описи
                actorToDelete.setRegister(null);
                register.getActors().remove(actorToDelete);
                //Уничтожаем ссылки предмета из дел
                for (Note note: actorToDelete.getNotes()){
                    note.getActors().remove(actorToDelete);
                }
                actorToDelete.setNotes(null);
                //Удаляем Дело
                actorRepository.deleteById(idToDelete);
                //Сохраняем новое состояние Описи
                registerRepository.save(register);
            }
        } else {
            log.debug("Опись с id {} не найдена:", registerId);
        }
    }

    @Override
    public void DeleteRelationWithNote(Long registerId, Long noteId, Long actorId) {
        //Ищем Опись с данным id
        Optional<Register> registerOptional = registerRepository.findById(registerId);
        if(registerOptional.isPresent()){
            Register register = registerOptional.get();

            //Ищем в описи именной указатель и дело
            Optional<Actor> actorOptional = register.getActors().stream()
                    .filter(actor -> actor.getId().equals(actorId))
                    .findFirst();

            Optional<Note> noteOptional = register.getNotes().stream()
                    .filter(note -> note.getId().equals(noteId))
                    .findFirst();


            if(actorOptional.isPresent() && noteOptional.isPresent()){

                Actor actor = actorOptional.get();
                Note note = noteOptional.get();

                //Уничтожаем ссылки
                actor.getNotes().remove(note);
                note.getActors().remove(actor);

                //Сохраняем новое состояние Описи
                registerRepository.save(register);
            }
        } else {
            log.debug("Опись с id {} не найдена:", registerId);
        }
    }
}
