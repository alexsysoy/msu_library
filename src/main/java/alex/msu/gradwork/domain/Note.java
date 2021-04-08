package alex.msu.gradwork.domain;

import alex.msu.gradwork.commands.NoteCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"register"})
@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long number;
    private Long numberOfSheets;
    private String text;

    @ManyToOne
    private Register register;

    @ManyToMany
    @JoinTable(name = "note_actor",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "note_subject",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects = new HashSet<>();


    public Note() {
    }

    public Note(Long number, Long numberOfSheets, String text) {
        this.number = number;
        this.numberOfSheets = numberOfSheets;
        this.text = text;
    }

    public Note(Long number, Long numberOfSheets, String text, Register register) {
        this.number = number;
        this.numberOfSheets = numberOfSheets;
        this.text = text;
        this.register = register;
    }

    public Note(Long id, Long number, Long numberOfSheets, String text, Register register, Set<Actor> actors, Set<Subject> subjects) {
        this.id = id;
        this.number = number;
        this.numberOfSheets = numberOfSheets;
        this.text = text;
        this.register = register;
        this.actors = actors;
        this.subjects = subjects;
    }

    //private String startDate;
//    private String endDate;
//    private String mark;
//    private String description;
//    @Lob
//    private Byte[] image;




}
