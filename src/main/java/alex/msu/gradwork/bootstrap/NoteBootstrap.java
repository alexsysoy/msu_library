package alex.msu.gradwork.bootstrap;

import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.repositories.ActorRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import alex.msu.gradwork.repositories.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component

public class NoteBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RegisterRepository registerRepository;
    private final ActorRepository actorRepository;
    private final SubjectRepository subjectRepository;

    public NoteBootstrap(RegisterRepository registerRepository, ActorRepository actorRepository, SubjectRepository subjectRepository) {
        this.registerRepository = registerRepository;
        this.actorRepository = actorRepository;
        this.subjectRepository = subjectRepository;
    }


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Loaded Bootstrap Data!");
        registerRepository.saveAll(getRegister());

    }


    private List<Register> getRegister(){

        List<Register> registers = new ArrayList(5);
        //get registers
        Register register1 = new Register();

        //get actors
        Actor actor1 = new Actor();
        actor1.setName("Петр");
        actor1.setPatronymic("Владимирович");
        actor1.setSurname("Страхов");
        actor1.setMemo("Интересная фамилия у человека");
        register1.addActor(actor1);
        actorRepository.save(actor1);

        Actor actor2 = new Actor();
        actor2.setName("Ираклий");
        actor2.setPatronymic("Виссарионович");
        actor2.setSurname("Павлиашвилли");
        actor2.setMemo("Ещё одна интересная личность");
        register1.addActor(actor2);
        actorRepository.save(actor2);

        Actor actor3 = new Actor();
        Actor actor4 = new Actor();


        //get subjects
        Subject subject1 = new Subject();
        subject1.setName("Народное просвещение");
        register1.addSubject(subject1);
        subjectRepository.save(subject1);

        Subject subject2 = new Subject();
        subject2.setName("Испытания");
        register1.addSubject(subject2);
        subjectRepository.save(subject2);

        Subject subject3 = new Subject();
        subject3.setName("Клименко");
        register1.addSubject(subject3);
        subjectRepository.save(subject3);

        Subject subject4 = new Subject();
        subject4.setName("Зачисление");
        register1.addSubject(subject4);
        subjectRepository.save(subject4);

        Subject subject5 = new Subject();
        subject5.setName("Страхов");
        register1.addSubject(subject5);
        subjectRepository.save(subject5);

        //get note
        Note note1 = new Note();
        note1.setAnnotation("Высочайшие приказы по Министерству Народного просвещения.");
        note1.setNumber(1L);
        note1.setMemo("Обеспечить массовое образование крестьянства");

        Note note2 = new Note();
        note2.setAnnotation("О принятии в число студентов Станислава Страхова");
        note2.setNumber(2L);
        note2.setMemo("Молодец Страхов!");

        Note note3 = new Note();
        note3.setAnnotation("О допущении разных лиц к испытанию на степень ДОКТОРА МЕДИЦИНЫ");
        note3.setNumber(3L);
        note3.setMemo("Испытания - это хорошо");

        Note note4 = new Note();
        note4.setAnnotation("О допущении разных лиц к испытанию на степень ЛЕКАРЯ.");
        note4.setNumber(4L);
        note4.setMemo("Испытания - это хорошо");

        Note note5 = new Note();
        note5.setAnnotation("О допущении разных лиц к испытанию на степень УЕЗДНОГО ВРАЧА ");
        note5.setNumber(5L);
        note5.setMemo("Испытания - это хорошо");

        Note note6 = new Note();
        note6.setAnnotation("О допущении разных лиц к испытанию на степень МАГИСТРА");
        note6.setNumber(6L);
        note6.setMemo("Испытания - это хорошо");

        Note note7 = new Note();
        note7.setAnnotation("О допущении разных лиц к испытанию на степень КАНДИДАТА");
        note7.setNumber(7L);
        note7.setMemo("Испытания - это хорошо");

        Note note8 = new Note();
        note8.setAnnotation("О допущении разных лиц к испытанию на степень ДАНТИСТА");
        note8.setNumber(8L);
        note8.setMemo("Испытания - это хорошо");

        Note note9 = new Note();
        note9.setAnnotation("О допущении к испытанию на степень провизора Ивана СИТНИКОВА и об утверждении его в этой степени");
        note9.setNumber(9L);
        note9.setMemo("Допускаем");

        Note note10 = new Note();
        note10.setAnnotation("О принятии в число студентов Митрофана КЛИМЕНКО");
        note10.setNumber(10L);
        note10.setMemo("Принимаем");

        Note note11 = new Note();
        note11.setAnnotation("О доставлении ведомости в Департамент Народного просвещения о выбывших студентах");
        note11.setNumber(11L);

        Note note12 = new Note();
        note12.setAnnotation("О доставлении г.  попечителю Московского учебного округа списка профессоров и преподавателей сего университета не присутствовавших на лекциях .");
        note12.setNumber(12L);

        Note note13 = new Note();
        note13.setAnnotation("Речь и отчет произнесенные в Торжественном собрании Московского универитета 12- го Января 1862 года.");
        note13.setNumber(13L);

        Note note14 = new Note();
        note14.setAnnotation("О назначении ординарному профессору Страхову 357 руб.  40 коп.  сер. за труды его за исправление должности во время заграничной командировки ординарного профессора БРАШМАНА");
        note14.setNumber(14L);
        note14.setMemo("Купил много учебников, наверное");


        //Народное просвещение
        note1.getSubjects().add(subject1);
        note11.getSubjects().add(subject1);
        note13.getSubjects().add(subject1);

        //Испытания
        note3.getSubjects().add(subject2);
        note4.getSubjects().add(subject2);
        note5.getSubjects().add(subject2);
        note6.getSubjects().add(subject2);
        note7.getSubjects().add(subject2);
        note8.getSubjects().add(subject2);

        //Клименко
        note10.getSubjects().add(subject3);


        //Зачисление
        note2.getSubjects().add(subject4);
        note10.getSubjects().add(subject4);

        //Страхов
        note2.getSubjects().add(subject5);
        note14.getSubjects().add(subject5);

        //Акторы
        //Павлиашвилл
        note2.getActors().add(actor1);

        //Страхов
        note2.getActors().add(actor2);
        note14.getActors().add(actor2);
        note7.getActors().add(actor2);


        register1.setName("Опись номер 701");
        register1.addNote(note1);
        register1.addNote(note2);
        register1.addNote(note3);
        register1.addNote(note4);
        register1.addNote(note5);
        register1.addNote(note6);
        register1.addNote(note7);
        register1.addNote(note8);
        register1.addNote(note9);
        register1.addNote(note10);
        register1.addNote(note11);
        register1.addNote(note12);
        register1.addNote(note13);
        register1.addNote(note14);

        register1.setAnnotation("Первая тестовая Опись");
        register1.setMemo("В Описи по умолчанию отсутствуют Изображения");

        Register register2 = new Register();
        register2.setName("Опись номер 702");
        Register register3 = new Register();
        register3.setName("Опись номер 703");
        Register register4 = new Register();
        register4.setName("Опись номер 704");

        registers.add(register1);
        registers.add(register2);
        registers.add(register3);
        registers.add(register4);


        return registers;

    }

}
