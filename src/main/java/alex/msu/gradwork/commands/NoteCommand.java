package alex.msu.gradwork.commands;

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
    private Long numberOfSheets;
    private String text;
    private Long registerId;

    private String findSubject;
    private String findActor;

    private Set<SubjectCommand> subjects = new HashSet<>();
    private Set<ActorCommand> actors = new HashSet<>();

}
