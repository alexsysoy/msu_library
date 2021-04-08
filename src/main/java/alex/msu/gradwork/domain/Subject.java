package alex.msu.gradwork.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"notes"})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String memo;

    @ManyToMany(mappedBy = "subjects")
    private Set<Note> notes;

    public Subject() {
    }

    public Subject(String name) {
        this.name = name;
    }

}
