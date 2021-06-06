package alex.msu.library.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
