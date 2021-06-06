package alex.msu.library.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"register", "image"})
@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Номер Дела
    private Long number;

    //Аннотация
    private String annotation;

    //Примечание
    private String memo;

    //Опись, в которой находится дело
    @ManyToOne
    private Register register;

    //Изображение
    @ManyToOne
    private Image image;

    //Именной указатель
    @ManyToMany
    @JoinTable(name = "note_actor",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> actors = new HashSet<>();

    //Предметный указатель
    @ManyToMany
    @JoinTable(name = "note_subject",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects = new HashSet<>();


    public Note() {
    }

}
