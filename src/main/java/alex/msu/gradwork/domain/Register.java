package alex.msu.gradwork.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"notes","subjects","actors"})
@Entity
@Table(name = "registers")
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Название Описи
    private String name;

    //Аннотация
    private String annotation;

    //Примечание
    private String memo;

    //Предметный указатель Описи
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "register")
    private Set<Subject> subjects = new HashSet<>();

    //Именной указатель данной описи
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "register")
    private Set<Actor> actors = new HashSet<>();

    //Дела данной Описи
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "register")
    private Set<Note> notes = new HashSet<>();

    //Конструкторы для удобного добавления Дела, Предметного указателя
    public Register addNote(Note note){
        note.setRegister(this);
        this.notes.add(note);
        return this;
    }

    public Register addSubject(Subject subject){
        subject.setRegister(this);
        this.subjects.add(subject);
        return this;
    }

    public Register addActor(Actor actor){
        actor.setRegister(this);
        this.actors.add(actor);
        return this;
    }

}
