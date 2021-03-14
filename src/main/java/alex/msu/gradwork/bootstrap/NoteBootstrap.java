package alex.msu.gradwork.bootstrap;

import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.repositories.ActorRepository;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class NoteBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final NoteRepository noteRepository;
    private final RegisterRepository registerRepository;
    private final ActorRepository actorRepository;

    public NoteBootstrap(NoteRepository noteRepository, RegisterRepository registerRepository, ActorRepository actorRepository) {
        this.noteRepository = noteRepository;
        this.registerRepository = registerRepository;
        this.actorRepository = actorRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Loaded Bootstrap Data!");
        //System.out.println("Loaded Bootstrap Data!");
        registerRepository.saveAll(getRegister());
    }

    private List<Register> getRegister() {

        List<Register> registers = new ArrayList<>(2);

        //get actors
        Optional<Actor> ostromyslenskiyActorOptional = actorRepository.findByName("ostromyslenskiy");

        if(ostromyslenskiyActorOptional.isEmpty()){
            throw new RuntimeException("Expected Actor Not Found");
        }

        Optional<Actor> taickovActorOptional = actorRepository.findByName("taickov");

        if(taickovActorOptional.isEmpty()){
            throw new RuntimeException("Expected Actor Not Found");
        }

        Optional<Actor> mcsActorOptional = actorRepository.findByName("MCS");

        if(mcsActorOptional.isEmpty()){
            throw new RuntimeException("Expected Actor Not Found");
        }

        Actor ostromyslenskiy = ostromyslenskiyActorOptional.get();
        Actor taickov = taickovActorOptional.get();
        Actor mcsActor = mcsActorOptional.get();



        //get notes
        Note noteFirst = new Note();
        noteFirst.setNumber(19L);
        noteFirst.setNumberOfSheets(4L);
        noteFirst.setText("О высылке штаб-лекарю Остромысленскому свидетельство на звание акушера");
        noteFirst.getActors().add(ostromyslenskiy);

        Note noteSecond = new Note();
        noteSecond.setNumber(20L);
        noteSecond.setNumberOfSheets(2L);
        noteSecond.setText("Письмо Тайдакова с приложением краткой летописи: о покорении Сибири и одной старинной медной монеты");
        noteSecond.getActors().add(taickov);


        //get registers
        Register superRegister = new Register();
        superRegister.setName("Super");
        superRegister.addNote(noteFirst);
        superRegister.addNote(noteSecond);

        registers.add(superRegister);


        return registers;
    }
}
