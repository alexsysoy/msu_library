package alex.msu.library.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"notes","register","dupSubject"})
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Название ключевого слова
    private String name;

    //Примечание
    private String memo;

    //Все Дела данного предметного указателя
    @ManyToMany(mappedBy = "subjects")
    private Set<Note> notes;

    //Опись, которой принадлежит данный предметный указатель
    @ManyToOne
    private Register register;

    @OneToOne(fetch = FetchType.LAZY)
    private DupSubject dupSubject;

    public Subject() {
    }

}
