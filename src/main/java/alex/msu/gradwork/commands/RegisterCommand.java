package alex.msu.gradwork.commands;

import alex.msu.gradwork.domain.Note;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RegisterCommand {

    private Long id;
    private Long number;
    private String name;

    private Set<NoteCommand> notes = new HashSet<>();
}
