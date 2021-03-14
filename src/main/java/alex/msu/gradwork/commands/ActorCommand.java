package alex.msu.gradwork.commands;

import alex.msu.gradwork.domain.Note;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ActorCommand {

    private Long id;

    private String type;
    private String name;
    private String title;
    private Set<Note> notes;
}
