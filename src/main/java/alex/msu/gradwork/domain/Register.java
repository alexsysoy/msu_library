package alex.msu.gradwork.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"notes"})
@Entity
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long number;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "register")
    private Set<Note> notes = new HashSet<>();

    public Register addNote(Note note){
        note.setRegister(this);
        this.notes.add(note);
        return this;
    }

}
