package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.converters.NoteToNoteCommand;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;
    private final RegisterRepository registerRepository;
    private final NoteToNoteCommand noteToNoteCommand;

    public NoteServiceImpl(NoteRepository noteRepository, RegisterRepository registerRepository, NoteToNoteCommand noteToNoteCommand) {
        this.noteRepository = noteRepository;
        this.registerRepository = registerRepository;
        this.noteToNoteCommand = noteToNoteCommand;
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
}
