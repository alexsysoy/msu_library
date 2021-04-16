package alex.msu.gradwork.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SubjectCommand {

    private Long id;
    private String name;
    private String memo;

}
