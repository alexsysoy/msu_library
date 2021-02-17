package alex.msu.gradwork.bootstrap;

import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class NoteBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final NoteRepository noteRepository;
    private final RegisterRepository registerRepository;

    public NoteBootstrap(NoteRepository noteRepository, RegisterRepository registerRepository) {
        this.noteRepository = noteRepository;
        this.registerRepository = registerRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Loaded Bootstrap Data!");
        System.out.println("Loaded Bootstrap Data!");
        noteRepository.saveAll(getNotes());
    }

    private List<Note> getNotes() {

        List<Note> notes = new ArrayList<>(2);


        //Note 1
        Note noteFirst = new Note();
        noteFirst.setNumber(1L);
        noteFirst.setNumberOfSheets(123L);
        noteFirst.setText("Первая запись");


        notes.add(noteFirst);

        //Note 2
        Note noteSecond = new Note();
        noteSecond.setNumber(5L);
        noteSecond.setNumberOfSheets(3L);
        noteSecond.setText("Вторая запись");

        notes.add(noteSecond);

        return notes;
    }
}
