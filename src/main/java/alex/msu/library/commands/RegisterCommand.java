package alex.msu.library.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class  RegisterCommand {

    private Long id;
    private String name;
    private String annotation;
    private String memo;

    private Set<NoteCommand> notes = new HashSet<>();
    private Set<SubjectCommand> subjects = new HashSet<>();
    private Set<ActorCommand> actors = new HashSet<>();

}
