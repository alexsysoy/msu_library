package alex.msu.gradwork.bootstrap;

import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.repositories.ActorRepository;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import alex.msu.gradwork.repositories.SubjectRepository;
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
    private final SubjectRepository subjectRepository;

    public NoteBootstrap(NoteRepository noteRepository, RegisterRepository registerRepository, ActorRepository actorRepository, SubjectRepository subjectRepository) {
        this.noteRepository = noteRepository;
        this.registerRepository = registerRepository;
        this.actorRepository = actorRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Loaded Bootstrap Data!");
        registerRepository.saveAll(getRegister());
    }

    private List<Register> getRegister() {

        List<Register> registers = new ArrayList<>(2);

        //get subjects
        Optional<Subject> superSubjectOptional = subjectRepository.findByName("Super");

        if(superSubjectOptional.isEmpty()){
            throw new RuntimeException("Expected Subject Not Found");
        }

        Subject superSub = superSubjectOptional.get();

        //get actors
        Optional<Actor> ostromyslenskiyActorOptional = actorRepository.findByName("ostromyslenskiy");

        if(ostromyslenskiyActorOptional.isEmpty()){
            throw new RuntimeException("Expected Actor Not Found");
        }

        Optional<Actor> taickovActorOptional = actorRepository.findByName("taickov");

        if(taickovActorOptional.isEmpty()){
            throw new RuntimeException("Expected Actor Not Found");
        }

        Optional<Actor> yurevActorOptional = actorRepository.findByName("Yurev");

        if(yurevActorOptional.isEmpty()){
            throw new RuntimeException("Expected Actor Not Found");
        }

        Optional<Actor> basovActorOptional = actorRepository.findByName("Basov");

        if(basovActorOptional.isEmpty()){
            throw new RuntimeException("Expected Actor Not Found");
        }

        Actor ostromyslenskiy = ostromyslenskiyActorOptional.get();
        Actor taickov = taickovActorOptional.get();
        Actor Yurev = yurevActorOptional.get();
        Actor basov = basovActorOptional.get();



        //get notes
        Note noteFirst = new Note();
        noteFirst.setNumber(19L);
        noteFirst.setNumberOfSheets(4L);
        noteFirst.setText("О высылке штаб-лекарю Остромысленскому свидетельство на звание акушера");
        noteFirst.getActors().add(ostromyslenskiy);
        noteFirst.getSubjects().add(superSub);

        Note noteSecond = new Note();
        noteSecond.setNumber(20L);
        noteSecond.setNumberOfSheets(2L);
        noteSecond.setText("Письмо Тайдакова с приложением краткой летописи: о покорении Сибири и одной старинной медной монеты");
        noteSecond.getActors().add(taickov);

        Note noteThird = new Note();
        noteThird.setNumber(22L);
        noteThird.setNumberOfSheets(5L);
        noteThird.setText("О дозволении Юрьеву с будущего академического года слушать лекции в университете");
        noteThird.getActors().add(Yurev);

        Note noteFourth = new Note();
        noteFourth.setNumber(19L);
        noteFourth.setNumberOfSheets(45L);
        noteFourth.setText("О покупке у прозектора Басова препоратов");
        noteFourth.getActors().add(basov);


        //get registers
        Register firstRegister = new Register();
        firstRegister.setName("Первый регистр");
        firstRegister.addNote(noteSecond);
        firstRegister.addNote(noteThird);

        Register secondRegister = new Register();
        secondRegister.setName("Второй регистр");
        secondRegister.addNote(noteFirst);
        secondRegister.addNote(noteFourth);

        registers.add(firstRegister);
        registers.add(secondRegister);


        return registers;
    }
}
