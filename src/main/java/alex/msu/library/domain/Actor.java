package alex.msu.library.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"notes","register"})
@Entity
@Table(name = "actors")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Имя
    private String name;

    //Отчество
    private String patronymic;

    //Фамилия
    private String surname;

    //Примечание
    private String memo;

    //Все Дела данного именного указателя
    @ManyToMany(mappedBy = "actors")
    private Set<Note> notes;

    //Опись, которой принадлежит данный предметный указатель
    @ManyToOne
    private Register register;

    @OneToOne(fetch = FetchType.LAZY)
    private DupActor dupActor;

    public Actor() {
    }
}
