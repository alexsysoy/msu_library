package alex.msu.gradwork.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"notes"})
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Название страницы изображения
    private String imageName;

    private Byte[] image;

    //Дела, связанные с данным изображением
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "image")
    private Set<Note> notes = new HashSet<>();
}
