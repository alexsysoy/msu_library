package alex.msu.library.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DupSubjectCommand {
    private Long id;
    private String name;
    private String memo;
}
