package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.domain.Register;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RegisterToRegisterCommand implements Converter<Register, RegisterCommand> {

    private final NoteToNoteCommand noteConverter;

    public RegisterToRegisterCommand(NoteToNoteCommand noteConverter) {
        this.noteConverter = noteConverter;
    }


    @Override
    public RegisterCommand convert(Register source) {

        if (source == null){
            return null;
        }

        RegisterCommand command = new RegisterCommand();
        command.setName(source.getName());
        command.setId(source.getId());
        command.setNumber(source.getNumber());


        if (source.getNotes() != null && source.getNotes().size()>0){
            source.getNotes()
                    .forEach(note -> command.getNotes().add(noteConverter.convert(note)));
        }

        return null;
    }
}
