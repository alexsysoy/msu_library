package alex.msu.library.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class NoteCommand {

    private Long id;
    private Long number;
    private String annotation;
    private String memo;

    //Даём не всю сущность, а только номера сущностей
    private Long registerId;
    private Long imageId;

    //Специальные поля для предметного и именного указателя
    private String findSubject;
    private String findNameActor;
    private String findPatronymicActor;
    private String findSurnameActor;

    private Set<SubjectCommand> subjects = new HashSet<>();
    private Set<ActorCommand> actors = new HashSet<>();

}
