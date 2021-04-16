package alex.msu.gradwork.commands;

import alex.msu.gradwork.domain.Note;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ManyToMany;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ActorCommand {

    private Long id;

    private String name;
    private String patronymic;
    private String surname;
    private String memo;

}
