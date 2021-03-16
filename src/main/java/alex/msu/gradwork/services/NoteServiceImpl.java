package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.converters.NoteCommandToNote;
import alex.msu.gradwork.converters.NoteToNoteCommand;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
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
    private final NoteToNoteCommand noteToNoteCommand;
    private final NoteCommandToNote noteCommandToNote;

    public NoteServiceImpl(NoteRepository noteRepository, RegisterRepository registerRepository, NoteToNoteCommand noteToNoteCommand, NoteCommandToNote noteCommandToNote) {
        this.noteRepository = noteRepository;
        this.registerRepository = registerRepository;
        this.noteToNoteCommand = noteToNoteCommand;
        this.noteCommandToNote = noteCommandToNote;
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

    @Override
    @Transactional
    public NoteCommand saveNoteCommand(NoteCommand command) {

        Optional<Register> registerOptional = registerRepository.findById(command.getRegisterId());

        if (registerOptional.isEmpty()){
            //todo error if not found
            log.error("Register not found for id: " + command.getRegisterId());
            return new NoteCommand();
        } else {
            Register register = registerOptional.get();

            Optional<Note> noteOptional = register
                    .getNotes()
                    .stream()
                    .filter(note -> note.getId().equals(command.getId()))
                    .findFirst();
            if (noteOptional.isPresent()){
                Note noteFound = noteOptional.get();
                noteFound.setText(command.getText());
                noteFound.setNumber(command.getNumber());
            } else {
                //add new Note
                Note note = noteCommandToNote.convert(command);
                note.setRegister(register);
                register.addNote(note);
            }

            Register saveRegister = registerRepository.save(register);

            Optional<Note> savedNoteOptional = saveRegister.getNotes().stream()
                    .filter(registerNotes -> registerNotes.getId().equals(command.getId()))
                    .findFirst();

            //check by text

            if(savedNoteOptional.isEmpty()){
                savedNoteOptional = saveRegister.getNotes().stream()
                        .filter(registerNotes -> registerNotes.getText().equals(command.getText()))
                        .filter(registerNotes -> registerNotes.getNumber().equals(command.getNumber()))
                        .findFirst();
            }

            //to do check for fail
            return noteToNoteCommand.convert(savedNoteOptional.get());
    }



    }
}
