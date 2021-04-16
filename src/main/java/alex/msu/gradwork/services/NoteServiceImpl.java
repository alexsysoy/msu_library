package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.converters.NoteCommandToNote;
import alex.msu.gradwork.converters.NoteToNoteCommand;
import alex.msu.gradwork.converters.SubjectCommandToSubject;
import alex.msu.gradwork.converters.SubjectToSubjectCommand;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import alex.msu.gradwork.repositories.SubjectRepository;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;
    private final RegisterRepository registerRepository;
    private final SubjectRepository subjectRepository;
    private final NoteToNoteCommand noteToNoteCommand;
    private final NoteCommandToNote noteCommandToNote;
    private final SubjectCommandToSubject subjectCommandToSubject;
    private final SubjectToSubjectCommand subjectToSubjectCommand;

    public NoteServiceImpl(NoteRepository noteRepository, RegisterRepository registerRepository, SubjectRepository subjectRepository, NoteToNoteCommand noteToNoteCommand, NoteCommandToNote noteCommandToNote, SubjectCommandToSubject subjectCommandToSubject, SubjectToSubjectCommand subjectToSubjectCommand) {
        this.noteRepository = noteRepository;
        this.registerRepository = registerRepository;
        this.subjectRepository = subjectRepository;
        this.noteToNoteCommand = noteToNoteCommand;
        this.noteCommandToNote = noteCommandToNote;
        this.subjectCommandToSubject = subjectCommandToSubject;
        this.subjectToSubjectCommand = subjectToSubjectCommand;
    }

    @Override
    public Set<Note> getNotes() {
        log.debug("I'm in the Service debug");
        System.out.println("I'm in the Service");

        Set<Note> notes = new HashSet<>();
        noteRepository.findAll().iterator().forEachRemaining(notes::add);
        return notes;
    }

    @Override
    public Note findById(Long l) {

        Optional<Note> noteOptional = noteRepository.findById(l);

        if (noteOptional.isEmpty()){
            throw new RuntimeException("Note not found!");
        }
        return noteOptional.get();
    }

    @Override
    public NoteCommand findByRegisterIdAndNoteId(Long registerId, Long noteId) {

        Optional<Register> registerOptional = registerRepository.findById(registerId);

        if (registerOptional.isEmpty()){
            //todo impl error handling
            log.error("register id not found. Id: " + registerId);
        }

        Register register = registerOptional.get();

        Optional<NoteCommand> noteCommandOptional = register.getNotes().stream()
                .filter(note -> note.getId().equals(noteId))
                .map(noteToNoteCommand::convert).findFirst();

        if (noteCommandOptional.isEmpty()){
            //todo impl error handling
            log.error("note id not found. Id: " + noteId);
        }

        return noteCommandOptional.get();
    }

    //todo return with Command
    @Override
    public Set<Note> findNoteCommand(NoteCommand command) {

        Optional<Register> registerOptional = registerRepository.findById(command.getRegisterId());

        if (registerOptional.isEmpty()) {
            //todo error if not found
            log.error("Register not found for id: " + command.getRegisterId());
            return new HashSet<Note>();
        } else {
            Register register = registerOptional.get();
            Set<Note> notes = new HashSet<>();
            for (Note note : register.getNotes()){
                if (note.getSubjects().stream().anyMatch(subject -> subject.getName().equals(command.getFindSubject()))){
                    notes.add(note);
                }
            }

            System.out.println(notes.size());
            return notes;
        }
    }



    @Override
    @Transactional
    @Synchronized
    public NoteCommand saveNoteCommand(NoteCommand command) {

        //todo error if not found
        Optional<Register> registerOptional = registerRepository.findById(command.getRegisterId());
        Register registerFound = registerOptional.get();
        Optional<Note> noteOptional = noteRepository.findById(command.getId());
        Note noteFound = noteOptional.get();

        //Ключевое слово передано, в getFindSubject находится текст
        log.debug("Ключевое слово: " + command.getFindSubject());

        Subject subjectFound;

        noteFound.setAnnotation(command.getAnnotation());
        noteFound.setNumber(command.getNumber());

        //Ищем в текущем Деле
        Optional<Subject> subjectOptional = noteFound
                .getSubjects()
                .stream()
                .filter(subject -> subject.getName().equals(command.getFindSubject()))
                .findFirst();

        if (subjectOptional.isPresent()) {
            //Проверка: есть ли ключевое слово в данном Деле
            subjectFound = subjectOptional.get();
            log.debug("Ключевое слово не меняем. В деле есть данное ключевое слово! номер id: " + subjectFound.getId());
            //noteFound.getSubjects().add(subjectFound);
        } else {
            //Ищем в текущей Описи
            Optional<Subject> subjectRegister = registerFound
                    .getSubjects()
                    .stream()
                    .filter((subject -> subject.getName().equals(command.getFindSubject())))
                    .findFirst();
            if (subjectRegister.isPresent()){
                //Нашли ключевое слово в описи
                //Добавляем ключевое слово к Делу
                subjectFound = subjectRegister.get();
                log.debug("в описи есть данное ключевое слово! номер id: " + subjectFound.getId());
                //Добавляем к предметному списку текущего Дела
                noteFound.getSubjects().add(subjectFound);
                //Добавляем к предметному списку ключевого слова Описи ссылку на данное дело
                //Делает hibernate
            } else {
                log.debug("в описи нет ключевого слова!");

                //В деле и в описи нет данного ключевого слова
                //Создаем новое ключевое слово
                subjectFound = new Subject();
                subjectFound.setName(command.getFindSubject());
                subjectRepository.save(subjectFound);

                //Сохраняем ключевое слово в предметном указателе Описи
                registerFound.addSubject(subjectFound);
                registerRepository.save(registerFound);
                //Сохраняем ключевое слово в предметном указателе Дела
                noteFound.getSubjects().add(subjectFound);
                log.debug("в описи нет ключевого слова! Создали новое номер id: " + subjectFound.getId());

            }
        }

        Note savedNote = noteRepository.save(noteFound);
        return noteToNoteCommand.convert(noteFound);
    }

    @Override
    public void DeleteById(Long registerId, Long idToDelete){
        log.debug("Deleting note: " + registerId + ":" + idToDelete);

        Optional<Register> registerOptional = registerRepository.findById(registerId);

        if(registerOptional.isPresent()){
            Register register = registerOptional.get();
            log.debug("found register");

            Optional<Note> noteOptional = register
                    .getNotes()
                    .stream()
                    .filter(note -> note.getId().equals(idToDelete))
                    .findFirst();

            if(noteOptional.isPresent()){
                log.debug("found note");
                Note noteToDelete = noteOptional.get();
                noteToDelete.setRegister(null);
                register.getNotes().remove(noteOptional.get());
                registerRepository.save(register);
            }
        } else {
            log.debug("Register Id Not found. Id:" + registerId);
        }
    }
}
