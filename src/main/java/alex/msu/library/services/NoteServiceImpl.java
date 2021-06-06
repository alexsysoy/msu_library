package alex.msu.library.services;

import alex.msu.library.commands.NoteCommand;
import alex.msu.library.converters.NoteCommandToNote;
import alex.msu.library.converters.NoteToNoteCommand;
import alex.msu.library.converters.SubjectCommandToSubject;
import alex.msu.library.converters.SubjectToSubjectCommand;
import alex.msu.library.domain.*;
import alex.msu.library.repositories.*;
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
public class NoteServiceImpl implements NoteService{

    private final ImageRepository imageRepository;
    private final NoteRepository noteRepository;
    private final RegisterRepository registerRepository;
    private final SubjectRepository subjectRepository;
    private final ActorRepository actorRepository;
    private final NoteToNoteCommand noteToNoteCommand;
    private final NoteCommandToNote noteCommandToNote;
    private final SubjectCommandToSubject subjectCommandToSubject;
    private final SubjectToSubjectCommand subjectToSubjectCommand;

    public NoteServiceImpl(ImageRepository imageRepository, NoteRepository noteRepository, RegisterRepository registerRepository, SubjectRepository subjectRepository, ActorRepository actorRepository, NoteToNoteCommand noteToNoteCommand, NoteCommandToNote noteCommandToNote, SubjectCommandToSubject subjectCommandToSubject, SubjectToSubjectCommand subjectToSubjectCommand) {
        this.imageRepository = imageRepository;
        this.noteRepository = noteRepository;
        this.registerRepository = registerRepository;
        this.subjectRepository = subjectRepository;
        this.actorRepository = actorRepository;
        this.noteToNoteCommand = noteToNoteCommand;
        this.noteCommandToNote = noteCommandToNote;
        this.subjectCommandToSubject = subjectCommandToSubject;
        this.subjectToSubjectCommand = subjectToSubjectCommand;
    }


    //Возращает постранично отсортированный список Дел по Id Описи
    @Override
    public Page<Note> findPaginated(Long registerId, int pageNumber, int pageSize, String sortField, String sortDirection) {

        final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return noteRepository.findNotesByRegisterId(registerId, pageable);
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

    // Расширенный поиск единиц хранения по данной описи
    @Override
    @Transactional
    @Synchronized
    public Set<Note> searchNotes(NoteCommand command) {

        Set<Note> notes = noteRepository.findNotesByRegisterId(command.getRegisterId());

        log.debug("Общее количество Дел в Описи {}", notes.size());

        // Ищем по номеру
        if (command.getNumber() != null) {
            notes = notes.stream().filter(note -> note.getNumber().equals(command.getNumber())).collect(Collectors.toSet());
            log.debug("Количество дел {} после поиска по номеру", notes.size());
        }


        // Ищем по заголовку дела
        if (!command.getAnnotation().isEmpty() && notes.size() > 0) {
            notes = notes.stream().filter(note -> note.getAnnotation().toLowerCase().strip().contains(command.getAnnotation().toLowerCase())).collect(Collectors.toSet());
            log.debug("Количество дел {} после поиска по заголовку дела", notes.size());
        }


        // Ищем по примечанию
        if (!command.getMemo().isEmpty() && notes.size() > 0) {
            notes = notes.stream()
                    .filter(note -> !(note.getMemo() == null))
                    .filter(note -> note.getMemo().toLowerCase().strip().contains(command.getMemo().toLowerCase())).collect(Collectors.toSet());
            log.debug("Количество дел {} после поиска по примечанию" , notes.size());
        }


        // Ищем по Предметному указателю
        if (!command.getFindSubject().isEmpty() && notes.size() > 0) {

            Set<Note> helpSet = new HashSet<>();

            for (Note note : notes) {

                if (!note.getSubjects().isEmpty()) {
                    for (Subject subject : note.getSubjects()) {
                        if (subject.getName().strip().toLowerCase().contains(command.getFindSubject().strip().toLowerCase())) helpSet.add(note);
                        if (subject.getDupSubject() != null){
                            if (subject.getDupSubject().getName().toLowerCase().strip().contains(command.getFindSubject().strip().toLowerCase())) helpSet.add(note);
                        }
                    }
                }
            }

            notes = helpSet;

            log.debug("Количество дел {} после поиска по предметному указателю и его дублю", notes.size());
        }

        if (!command.getFindNameActor().isEmpty()) {
            Set<Note> helpSet = new HashSet<>();

            for (Note note : notes) {
                if (!note.getActors().isEmpty()) {
                    for (Actor actor : note.getActors()) {
                        if (actor.getName().strip().toLowerCase().contains(command.getFindNameActor().strip().toLowerCase())) helpSet.add(note);
                        if (actor.getDupActor() != null) {
                            if (actor.getDupActor().getName().strip().toLowerCase().contains(command.getFindNameActor().strip().toLowerCase())) helpSet.add(note);
                        }
                    }
                }
            }

            notes = helpSet;
            log.debug("Количество дел {} после поиска по имени", notes.size());
        }

        if (!command.getFindPatronymicActor().isEmpty()) {
            Set<Note> helpSet = new HashSet<>();

            for (Note note : notes) {
                if (!note.getActors().isEmpty()) {
                    for (Actor actor : note.getActors()) {
                        if (actor.getPatronymic().strip().toLowerCase().contains(command.getFindPatronymicActor().strip().toLowerCase())) helpSet.add(note);
                        if (actor.getDupActor() != null) {
                            if (actor.getDupActor().getPatronymic().strip().toLowerCase().contains(command.getFindPatronymicActor().strip().toLowerCase())) helpSet.add(note);
                        }
                    }
                }
            }

            notes = helpSet;
            log.debug("Количество дел {} после поиска по отчеству", notes.size());
        }

        if (!command.getFindSurnameActor().isEmpty()) {
            Set<Note> helpSet = new HashSet<>();

            for (Note note : notes) {
                if (!note.getActors().isEmpty()) {
                    for (Actor actor : note.getActors()) {
                        if (actor.getSurname().strip().toLowerCase().contains(command.getFindSurnameActor().strip().toLowerCase())) helpSet.add(note);
                        if (actor.getDupActor() != null) {
                            if (actor.getDupActor().getSurname().strip().toLowerCase().contains(command.getFindSurnameActor().strip().toLowerCase())) helpSet.add(note);
                        }
                    }
                }
            }

            notes = helpSet;
            log.debug("Количество дел {} после поиска по фамилии", notes.size());
        }

//        if ( || !command.getFindPatronymicActor().isEmpty() || !command.getFindSurnameActor().isEmpty() && notes.size() > 0) {
//
//            Set<Note> helpSet = new HashSet<>();
//
//            for (Note note : notes) {
//                if (!note.getActors().isEmpty()) {
//                    for (Actor actor : note.getActors()) {
//                        if (actor.getName().strip().toLowerCase().contains(command.getFindNameActor().strip().toLowerCase())
//                                ||
//                                actor.getPatronymic().strip().toLowerCase().contains(command.getFindPatronymicActor().strip().toLowerCase())
//                                ||
//                                actor.getSurname().strip().toLowerCase().contains(command.getFindSurnameActor().strip().toLowerCase())) helpSet.add(note);
//                        if (actor.getDupActor() != null) {
//                            if (actor.getDupActor().getName().toLowerCase().strip().contains(command.getFindNameActor().strip().toLowerCase())) helpSet.add(note);
//                            if (actor.getDupActor().getPatronymic().toLowerCase().strip().contains(command.getFindPatronymicActor().strip().toLowerCase())) helpSet.add(note);
//                            if (actor.getDupActor().getSurname().toLowerCase().strip().contains(command.getFindSurnameActor().strip().toLowerCase())) helpSet.add(note);
//                        }
//                    }
//                }
//            }
//
//            notes = helpSet;
//
//        }


//        // Ищем по Именному указателю
//        if (!command.getFindNameActor().isEmpty() || !command.getFindPatronymicActor().isEmpty() || !command.getFindSurnameActor().isEmpty() && notes.size() > 0) {
//            notes = notes.stream()
//                    .filter(note -> note.getActors().stream()
//                            .anyMatch(actor -> actor.getName().toLowerCase().contains(command.getFindNameActor().toLowerCase().trim())))
//                    .filter(note -> note.getActors().stream()
//                            .anyMatch(actor -> actor.getPatronymic().toLowerCase().contains(command.getFindPatronymicActor().toLowerCase().trim())))
//                    .filter(note -> note.getActors().stream()
//                            .anyMatch(actor -> actor.getSurname().toLowerCase().contains(command.getFindSurnameActor().toLowerCase().trim())))
//                    .collect(Collectors.toSet());
//            log.debug("Количество дел {} после поиска по именному указателю", notes.size());
//        }

        return notes;
    }


    //Редактируем или сохраняем Дело
    @Override
    @Transactional
    @Synchronized
    public NoteCommand saveNoteCommand(NoteCommand command) {

        Optional<Register> registerOptional = registerRepository.findById(command.getRegisterId());
        Register registerFound = registerOptional.get();
        Optional<Note> noteOptional = noteRepository.findById(command.getId());
        Note noteFound = noteOptional.get();


        noteFound.setAnnotation(command.getAnnotation());
        noteFound.setNumber(command.getNumber());
        noteFound.setMemo(command.getMemo());

        //Ищем Предметный указатель в текущем Деле если он введён
        if (!command.getFindSubject().isEmpty()){
            Optional<Subject> subjectOptional;
            subjectOptional = noteFound
                    .getSubjects()
                    .stream()
                    .filter(subject -> subject.getName().equals(command.getFindSubject()))
                    .findFirst();

            subjectOptional.ifPresent(subject -> log.debug("Предметный указатель в Деле есть. Номер id: " + subject.getId()));

            //Ищем предметный указатель в текущей Описи
            if (subjectOptional.isEmpty()){
                subjectOptional = registerFound
                        .getSubjects()
                        .stream()
                        .filter((subject -> subject.getName().equals(command.getFindSubject())))
                        .findFirst();
                if (subjectOptional.isPresent()) {
                    noteFound.getSubjects().add(subjectOptional.get());
                    log.debug("В описи есть данный Предметный указатель. Номер id: " + subjectOptional.get().getId());
                } else {
                    log.debug("В описи нет данного Предметного указателя!");

                    //В деле и в описи нет данного ключевого слова
                    //Создаем новое ключевое слово
                    Subject subject = new Subject();
                    subject.setName(command.getFindSubject());
                    subjectRepository.save(subject);

                    //Сохраняем ключевое слово в предметном указателе Описи
                    registerFound.addSubject(subject);
                    registerRepository.save(registerFound);
                    //Сохраняем ключевое слово в предметном указателе Дела
                    noteFound.getSubjects().add(subject);
                    log.debug("Создали новый Предметный указатель, номер id: " + subject.getId());
                }
            }
        }



        // Ищем Именной указатель в текущем деле
        Optional<Actor> actorOptional;

        // Проверяем, заполнено ли поля Именного указателя (или имя, или отчество, или фамилия)
        if (!command.getFindNameActor().isEmpty() || !command.getFindPatronymicActor().isEmpty() || !command.getFindSurnameActor().isEmpty()) {

            //Ищем Именной указатель в текущем Деле
            actorOptional = noteFound.getActors().stream()
                    .filter(actor -> actor.getName().equals(command.getFindNameActor()))
                    .filter(actor -> actor.getPatronymic().equals(command.getFindPatronymicActor()))
                    .filter(actor -> actor.getSurname().equals(command.getFindSurnameActor()))
                    .findFirst();

            actorOptional.ifPresent(actor -> log.debug("Актор не меняем. В Деле есть. Номер id: " + actor.getId()));

            //Ищем Именной указатель в текущей Описи
            actorOptional = registerFound.getActors().stream()
                    .filter(actor -> actor.getName().equals(command.getFindNameActor()))
                    .filter(actor -> actor.getPatronymic().equals(command.getFindPatronymicActor()))
                    .filter(actor -> actor.getSurname().equals(command.getFindSurnameActor()))
                    .findFirst();

            if (actorOptional.isPresent()) {
                log.debug("В описи данный Актор есть. Номер id: " + actorOptional.get().getId());
                noteFound.getActors().add(actorOptional.get());
            }


            if (actorOptional.isEmpty()) {
                log.debug("В описи нет данного Актора");

                //В деле и в описи нет данного Актора
                //Создаем новый Актор
                Actor actor = new Actor();
                actor.setName(command.getFindNameActor());
                actor.setPatronymic(command.getFindPatronymicActor());
                actor.setSurname(command.getFindSurnameActor());
                actorRepository.save(actor);

                //Сохраняем Актор в предметном указателе Описи
                registerFound.addActor(actor);
                registerRepository.save(registerFound);
                //Сохраняем Актор в предметном указателе Дела
                noteFound.getActors().add(actor);
                log.debug("Создали нового Актора, номер id: " + actor.getId());
            }
        }

        Note savedNote = noteRepository.save(noteFound);
        return noteToNoteCommand.convert(savedNote);
    }


    // Создание нового Дела
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
        if (!command.getFindSubject().isEmpty()) {

            String subjectName = command.getFindSubject().toLowerCase().trim();
            //Ищем предметный указатель в Описи
            Optional<Subject> subjectRegister = registerFound
                    .getSubjects()
                    .stream()
                    .filter((subject -> subject.getName().toLowerCase().equals(subjectName)))
                    .findFirst();
            if (subjectRegister.isPresent()) {
                log.debug("В описи есть данный Предметный указатель! номер id: " + subjectRegister.get().getId() + " добавляем его");
                //Добавляем к предметному списку текущего Дела
                note.getSubjects().add(subjectRegister.get());

            } else {
                //Если не нашли предметный указатель, создаём его
                Subject subject = new Subject();
                subject.setName(command.getFindSubject().trim());
                subjectRepository.save(subject);

                //Сохраняем Предметный указатель в Предметном указателе Описи
                registerFound.addSubject(subject);
                registerRepository.save(registerFound);
                //Сохраняем Предметный указатель в Предметный указателе Дела
                note.getSubjects().add(subject);
            }
        }

        //Если был добавлен Именной указатель (или имя, или отчество, или фамилия)
        if (!command.getFindNameActor().isEmpty() || !command.getFindPatronymicActor().isEmpty() || !command.getFindSurnameActor().isEmpty()) {

            String name = command.getFindNameActor().trim().toLowerCase();
            String patronymic = command.getFindPatronymicActor().trim().toLowerCase();
            String surname = command.getFindSurnameActor().trim().toLowerCase();
            //Ищем в текущей Описи
            Optional<Actor> actorOptional = registerFound.getActors()
                    .stream()
                    .filter(actor -> actor.getName().toLowerCase().equals(name) && actor.getPatronymic().toLowerCase().equals(patronymic) && actor.getSurname().toLowerCase().equals(surname))
                    .findFirst();

                if (actorOptional.isPresent()){
                    note.getActors().add(actorOptional.get());
                } else {
                    Actor actor = new Actor();
                    actor.setName(command.getFindNameActor().trim());
                    actor.setPatronymic(command.getFindPatronymicActor().trim());
                    actor.setSurname(command.getFindSurnameActor().trim());
                    actorRepository.save(actor);

                    //Сохраняем Актор в предметном указателе Описи
                    registerFound.addActor(actor);
                    //registerRepository.save(registerFound);
                    //Сохраняем Актор в предметном указателе Дела
                    note.getActors().add(actor);
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

    //Ищем Дело по id Изображения и Дела
    @Override
    public NoteCommand findByImageIdAndNoteId(Long imageId, Long noteId) {

        Optional<Image> imageOptional = imageRepository.findById(imageId);

        if(imageOptional.isEmpty()){
            log.error("Изображения нет. id: " + imageId);
        }

        Image image = imageOptional.get();

        Optional<NoteCommand> noteCommandOptional = image.getNotes().stream()
                .filter(note -> note.getId().equals(noteId))
                .map(noteToNoteCommand::convert)
                .findFirst();

        if(noteCommandOptional.isEmpty()){
            log.error("Дело не найдено. id: " + noteId);
        }

        return noteCommandOptional.get() ;
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
