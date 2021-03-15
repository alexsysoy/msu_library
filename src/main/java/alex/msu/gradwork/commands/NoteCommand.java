package alex.msu.gradwork.commands;

import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.domain.Register;
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
    private Register register;
    private Set<Actor> actors = new HashSet<>();

}
