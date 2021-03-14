package alex.msu.gradwork.services;

import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.repositories.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
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
}
