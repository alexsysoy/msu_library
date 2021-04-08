package alex.msu.gradwork.bootstrap;

import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.repositories.ActorRepository;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import alex.msu.gradwork.repositories.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BootstrapRegister {

    private final NoteRepository noteRepository;
    private final RegisterRepository registerRepository;
    private final ActorRepository actorRepository;
    private final SubjectRepository subjectRepository;

    public BootstrapRegister(NoteRepository noteRepository, RegisterRepository registerRepository, ActorRepository actorRepository, SubjectRepository subjectRepository) {
        this.noteRepository = noteRepository;
        this.registerRepository = registerRepository;
        this.actorRepository = actorRepository;
        this.subjectRepository = subjectRepository;
    }


    public List<Register> getRegister() {

        List<Register> registers = new ArrayList<>();

        //get subjects
        Subject GosNaslednik = subjectRepository.findByName("Государь Наследник").get();
        Subject Knigi = subjectRepository.findByName("Книги").get();
        Subject Suvorov = subjectRepository.findByName("Суворов").get();
        Subject Strahov = subjectRepository.findByName("Страхов").get();

        //get notes
        Note note10 = new Note();
        note10.setNumber(10L);
        note10.setNumberOfSheets(126L);
        note10.setText("О книгах поступающих в университетскую библиотеку");
        note10.getSubjects().add(Knigi);

        Note note83 = new Note();
        note83.setNumber(83L);
        note83.setNumberOfSheets(16L);
        note83.setText("О напечатании разных книг для руководства студентам 2 го отделения философского факультета");
        note83.getSubjects().add(Knigi);

        Note note95 = new Note();
        note95.setNumber(95L);
        note95.setNumberOfSheets(12L);
        note95.setText("С циркуляром в пояснение к ст 3 положения 28 ноября 1836 года о пенсии для Министра Народного Просвещения");
        note95.getSubjects().add(Suvorov);
        note95.getSubjects().add(Knigi);

        Note note145 = new Note();
        note145.setNumber(145L);
        note145.setNumberOfSheets(2L);
        note145.setText("С донесением О.П. Страхова, что он по исполнении дозволения от Его Высокородия Г.Ректора, вступил в законный брак");
        note145.getSubjects().add(Strahov);

        Note note149 = new Note();
        note149.setNumber(149L);
        note149.setNumberOfSheets(1L);
        note149.setText("О посещенн Государем Наследником университета");
        note149.getSubjects().add(GosNaslednik);
        note149.getSubjects().add(Suvorov);
        note149.getSubjects().add(Strahov);

        Note note174 = new Note();
        note174.setNumber(174L);
        note174.setNumberOfSheets(50L);
        note174.setText("О передаче от О.П. Страхова в ведении Г.Амфельда инструментов для планов судебной медицины и медицинской полиции");
        note174.getSubjects().add(Strahov);

        //get registers

        Register registerNumberSix = new Register();
        registerNumberSix.setName("Опись номер 6");
        registerNumberSix.addNote(note10);
        registerNumberSix.addNote(note83);
        registerNumberSix.addNote(note95);
        registerNumberSix.addNote(note149);
        registerNumberSix.addNote(note145);
        registerNumberSix.addNote(note174);
        registers.add(registerNumberSix);

        return registers;
    }
}
