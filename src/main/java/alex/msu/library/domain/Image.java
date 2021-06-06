package alex.msu.library.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"notes"})
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Название страницы изображения
    private String imageName;

    @Lob
    private Byte[] bytes;

    //Дела, связанные с данным изображением
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "image")
    private Set<Note> notes = new HashSet<>();

    //Конструкторы для удобного добавления Дела
    public Image addNote(Note note){
        note.setImage(this);
        this.notes.add(note);
        return this;
    }
}
