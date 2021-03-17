package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.domain.Register;
import lombok.Synchronized;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;


@Component
public class RegisterCommandToRegister implements Converter<RegisterCommand, Register> {


    private final NoteCommandToNote noteConverter;

    public RegisterCommandToRegister(NoteCommandToNote noteConverter) {
        this.noteConverter = noteConverter;
    }


    @Override
    @Synchronized
    @Nullable
    public Register convert(RegisterCommand command) {

        if (command == null){
            return null;
        }

        final Register register = new Register();
        register.setId(command.getId());
        register.setNumber(command.getNumber());
        register.setName(command.getName());

        if (command.getNotes() != null && command.getNotes().size()>0){
            command.getNotes()
                    .forEach(note -> register.getNotes().add(noteConverter.convert(note)));
        }

        return register;
    }
}
