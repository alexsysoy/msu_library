package alex.msu.gradwork.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
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


//    public Note() {
//
//    }
//
//    public Note(Long number, Long numberOfSheets, String text) {
//        this.number = number;
//        this.numberOfSheets = numberOfSheets;
//        this.text = text;
//    }
//
//    public Note(Long number, Long numberOfSheets, String text, Register register) {
//        this.number = number;
//        this.numberOfSheets = numberOfSheets;
//        this.text = text;
//        this.register = register;
//    }

    //private String startDate;
//    private String endDate;
//    private String mark;
//    private String description;
//    @Lob
//    private Byte[] image;




}
