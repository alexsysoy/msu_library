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
    public NoteCommand saveNoteCommand(NoteCommand command) {

        //todo error if not found
        Optional<Register> registerOptional = registerRepository.findById(command.getRegisterId());
        Register registerFound = registerOptional.get();
        Optional<Note> noteOptional = noteRepository.findById(command.getId());
        Note noteFound = noteOptional.get();

        //Ключевое слово передано
        System.out.println(command.getFindSubject());

        Subject subjectFound = new Subject();

        noteFound.setText(command.getText());
        noteFound.setNumber(command.getNumber());

        Optional<Subject> subjectOptional = noteFound
                .getSubjects()
                .stream()
                .filter(subject -> subject.getName().equals(command.getFindSubject()))
                .findFirst();

        if (subjectOptional.isPresent()){
            //useless
            subjectFound = subjectOptional.get();
            noteFound.getSubjects().add(subjectFound);
            System.out.println("тут есть данное ключевое слово! пропускаем");
        } else {
            //todo not good algorithm
            Optional<Subject> subjectRegisterOptional = Optional.of(new Subject());
            for (Note note : registerFound.getNotes()){
                log.debug("register id: " + registerFound.getId());
                log.debug("note id: " + note.getId());
                //System.out.println("FIND: " + note.getId());
                subjectRegisterOptional = note.getSubjects().stream().filter(x -> x.getName().equals(command.getFindSubject())).findFirst();
            }

            if (subjectRegisterOptional.isPresent()){
                System.out.println("TRUE");
                subjectFound = subjectRegisterOptional.get();
                noteFound.getSubjects().add(subjectFound);
                log.debug("Added old keyword: " + subjectFound.getName());
            }

        }

//        Ingredient ingredient = ingredientCommandToIngredient.convert(command);
//        ingredient.setRecipe(recipe);
//        recipe.addIngredient(ingredient);


        Note savedNote = noteRepository.save(noteFound);






//        Optional<Register> registerOptional = registerRepository.findById(command.getRegisterId());
//
//        if (registerOptional.isEmpty()){
//
//            log.error("Register not found for id: " + command.getRegisterId());
//            return new NoteCommand();
//        } else {
//            Register register = registerOptional.get();
//
//            Optional<Note> noteOptional = register
//                    .getNotes()
//                    .stream()
//                    .filter(note -> note.getId().equals(command.getId()))
//                    .findFirst();
//            if (noteOptional.isPresent()){
//                Note noteFound = noteOptional.get();
//                noteFound.setText(command.getText());
//                noteFound.setNumber(command.getNumber());
//
//                Optional<Subject> subjectOptional = noteFound
//                        .getSubjects()
//                        .stream()
//                        .filter((subject -> subject.getName().equals(command.getFindSubject())))
//                        .findFirst();
//                if (subjectOptional.isPresent()){
//                    Subject subjectFound = subjectOptional.get();
//                    noteFound.getSubjects().add(subjectRepository.findById(subjectFound.getId()).get());
//                    System.out.println("WARNING!: " + noteFound.getSubjects().size());
//                    Note savedNote = noteRepository.save(noteFound);
//                }
//
//
//            } else {
//                //add new Note
//                Note note = noteCommandToNote.convert(command);
//                note.setRegister(register);
//                register.addNote(note);
//            }
//
//
//
//            Register saveRegister = registerRepository.save(register);

//            Optional<Note> savedNoteOptional = saveRegister.getNotes().stream()
//                    .filter(registerNotes -> registerNotes.getId().equals(command.getId()))
//                    .findFirst();

            //check by text
//
//            if(savedNoteOptional.isEmpty()){
//                savedNoteOptional = saveRegister.getNotes().stream()
//                        .filter(registerNotes -> registerNotes.getText().equals(command.getText()))
//                        .filter(registerNotes -> registerNotes.getNumber().equals(command.getNumber()))
//                        .findFirst();
//            }

            //to do check for fail
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
