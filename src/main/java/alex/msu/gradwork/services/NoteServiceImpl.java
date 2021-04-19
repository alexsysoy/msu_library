package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.converters.NoteCommandToNote;
import alex.msu.gradwork.converters.NoteToNoteCommand;
import alex.msu.gradwork.converters.SubjectCommandToSubject;
import alex.msu.gradwork.converters.SubjectToSubjectCommand;
import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.repositories.ActorRepository;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import alex.msu.gradwork.repositories.SubjectRepository;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.Soundbank;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;
    private final RegisterRepository registerRepository;
    private final SubjectRepository subjectRepository;
    private final ActorRepository actorRepository;
    private final NoteToNoteCommand noteToNoteCommand;
    private final NoteCommandToNote noteCommandToNote;
    private final SubjectCommandToSubject subjectCommandToSubject;
    private final SubjectToSubjectCommand subjectToSubjectCommand;

    public NoteServiceImpl(NoteRepository noteRepository, RegisterRepository registerRepository, SubjectRepository subjectRepository, ActorRepository actorRepository, NoteToNoteCommand noteToNoteCommand, NoteCommandToNote noteCommandToNote, SubjectCommandToSubject subjectCommandToSubject, SubjectToSubjectCommand subjectToSubjectCommand) {
        this.noteRepository = noteRepository;
        this.registerRepository = registerRepository;
        this.subjectRepository = subjectRepository;
        this.actorRepository = actorRepository;
        this.noteToNoteCommand = noteToNoteCommand;
        this.noteCommandToNote = noteCommandToNote;
        this.subjectCommandToSubject = subjectCommandToSubject;
        this.subjectToSubjectCommand = subjectToSubjectCommand;
    }

    //Возвращает общее количество записей
    @Override
    public Long getTotalNotes(){
        return noteRepository.count();
    }

    //Возращает постранично отсортированный список Дел
    @Override
    public Page<Note> findPaginated(int pageNumber, int pageSize, String sortField, String sortDirection) {
        final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return noteRepository.findAll(pageable);
    }

    @Override
    public Set<Note> getNotes() {
        Set<Note> notes = new HashSet<>();
        noteRepository.findAll().iterator().forEachRemaining(notes::add);
        return notes;
    }

    //Возвращает Дело по номеру Дела
    @Override
    public Note findByNumber(Long l) {
        Optional<Note> noteOptional = noteRepository.findNoteByNumber(l);

        if (noteOptional.isEmpty()){
            throw new RuntimeException("Note not found!");
        }
        return noteOptional.get();
    }

    //Возвращает Дело по Id
    @Override
    public Note findById(Long l) {
        Optional<Note> noteOptional = noteRepository.findById(l);

        if (noteOptional.isEmpty()){
            throw new RuntimeException("Note not found!");
        }
        return noteOptional.get();
    }

    //Удаляет дело по номеру Дела и по номеру Описи
    @Override
    public void DeleteById(Long registerId, Long idToDelete){
        log.debug("Удаляем Дело id {} и id Описи {}", idToDelete, registerId);

        //Ищем Опись с данным id
        Optional<Register> registerOptional = registerRepository.findById(registerId);
        if(registerOptional.isPresent()){
            Register register = registerOptional.get();

            //Ищем Дело с данным id
            Optional<Note> noteOptional = register
                    .getNotes()
                    .stream()
                    .filter(note -> note.getId().equals(idToDelete))
                    .findFirst();

            if(noteOptional.isPresent()){

                Note noteToDelete = noteOptional.get();
                //Уничтожаем ссылки Дела
                //todo удалить ссылки на другие объекты
                noteToDelete.setRegister(null);
                //Удаляем Дело из списка Описи
                register.getNotes().remove(noteToDelete);
                //Удаляем Дело
                noteRepository.deleteById(idToDelete);
                //Сохраняем новое состояние Описи
                registerRepository.save(register);
            }
        } else {
            log.debug("Опись с id {} не найдена:", registerId);
        }
    }


    //Редактируем или сохраняем Дело
    @Override
    @Transactional
    @Synchronized
    public NoteCommand saveNoteCommand(NoteCommand command) {

        //todo error if not found
        Optional<Register> registerOptional = registerRepository.findById(command.getRegisterId());
        Register registerFound = registerOptional.get();
        Optional<Note> noteOptional = noteRepository.findById(command.getId());
        Note noteFound = noteOptional.get();


        noteFound.setAnnotation(command.getAnnotation());
        noteFound.setNumber(command.getNumber());
        noteFound.setMemo(command.getMemo());

        Subject subjectFound;

        //Ищем Предметный указатель в текущем Деле
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

        Actor actorFound = null;

        // Разбиваем findActor на массив слов
        String[] parts = command.getFindActor().split(" ");
        // Предположим, что вводятся все три слова по порядку (возможно, если слова нет вводится - )
        if (parts.length > 1) {
            //Ищем Именной указатель в текущем Деле
            //Ищем Имя
            for (Actor actor : noteFound.getActors()){
                if (actor.getName().equals(parts[0])  && actor.getPatronymic().equals(parts[1]) && actor.getSurname().equals(parts[2])){
                    actorFound = actor;
                    log.debug("Актор не меняем. В деле есть! номер id: " + actorFound.getId());
                }
            }

            if (actorFound == null) {
                //В Деле данный Актор не найден, ищем в текущей Описи
                for (Actor actor : registerFound.getActors()){
                    if (actor.getName().equals(parts[0])  && actor.getPatronymic().equals(parts[1]) && actor.getSurname().equals(parts[2])){
                        actorFound = actor;
                        log.debug("В описи данный Актор есть! номер id: " + actorFound.getId());
                        //Нашли Актор в описи
                        //Добавляем Актор к Делу
                        noteFound.getActors().add(actorFound);
                    }
                }
            }

            if (actorFound == null) {
                log.debug("в описи нет Актора!");

                //В деле и в описи нет данного Актора
                //Создаем новый Актор
                actorFound = new Actor();
                actorFound.setName(parts[0]);
                actorFound.setPatronymic(parts[1]);
                actorFound.setSurname(parts[2]);
                actorRepository.save(actorFound);

                //Сохраняем Актор в предметном указателе Описи
                registerFound.addActor(actorFound);
                registerRepository.save(registerFound);
                //Сохраняем Актор в предметном указателе Дела
                noteFound.getActors().add(actorFound);
                log.debug("в описи нет ключевого слова! Создали новое номер id: " + actorFound.getId());
            }
        }



        Note savedNote = noteRepository.save(noteFound);
        return noteToNoteCommand.convert(noteFound);
    }

    //Создание нового Дела
    @Override
    @Transactional
    @Synchronized
    public NoteCommand createNoteCommand(NoteCommand command) {

        // Актуализируем текущую Опись
        Optional<Register> registerOptional = registerRepository.findById(command.getRegisterId());
        Register registerFound = registerOptional.get();

        // Создаём Дело
        Note note = new Note();

        // Записываем поля
        note.setNumber(command.getNumber());
        note.setAnnotation(command.getAnnotation());
        note.setMemo(command.getMemo());

        //Если был добавлен предметный указатель
        if (command.getFindSubject() != null) {
            //Ищем предметный указатель
            Subject subjectFound;
            Optional<Subject> subjectRegister = registerFound
                    .getSubjects()
                    .stream()
                    .filter((subject -> subject.getName().equals(command.getFindSubject())))
                    .findFirst();
            if (subjectRegister.isPresent()) {
                //Нашли Пердметный указатель в описи
                //Добавляем его к Делу
                subjectFound = subjectRegister.get();
                log.debug("В описи есть данный Предметный указатель! номер id: " + subjectFound.getId() + " добавляем его");
                //Добавляем к предметному списку текущего Дела
                note.getSubjects().add(subjectFound);
                //Добавляем к предметному списку ключевого слова Описи ссылку на данное дело
                //Делает hibernate
            } else {
                //Если не нашли предметный указатель, создаём его
                subjectFound = new Subject();
                subjectFound.setName(command.getFindSubject());
                subjectRepository.save(subjectFound);

                //Сохраняем ключевое слово в предметном указателе Описи
                registerFound.addSubject(subjectFound);
                registerRepository.save(registerFound);
                //Сохраняем ключевое слово в предметном указателе Дела
                note.getSubjects().add(subjectFound);
            }
        }

        //Если был добавлен именной указатель
        if (command.getFindActor() != null) {
            // Разбиваем findActor на массив слов
            String[] parts = command.getFindActor().split(" ");
            // Предположим, что вводятся все три слова по порядку (возможно, если слова нет вводится - )
            if (parts.length > 1) {
                //Ищем в текущей Описи

                Optional<Actor> actorOptional = registerFound.getActors()
                        .stream()
                        .filter(actor -> actor.getName().equals(parts[0]) && actor.getPatronymic().equals(parts[1]) && actor.getSurname().equals(parts[2]))
                        .findFirst();
                if (actorOptional.isPresent()){
                    note.getActors().add(actorOptional.get());
                } else {
                    Actor actor = new Actor();
                    actor.setName(parts[0]);
                    actor.setPatronymic(parts[1]);
                    actor.setSurname(parts[2]);
                    actorRepository.save(actor);

                    //Сохраняем Актор в предметном указателе Описи
                    registerFound.addActor(actor);
                    //registerRepository.save(registerFound);
                    //Сохраняем Актор в предметном указателе Дела
                    note.getActors().add(actor);
                }
            }
        }

        //Сохраняем дело
        Note savedNote = noteRepository.save(note);
        //Добавляем дело в Опись
        registerFound.addNote(note);
        //Сохраняем опись
        registerRepository.save(registerFound);

        return noteToNoteCommand.convert(note);
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




}
