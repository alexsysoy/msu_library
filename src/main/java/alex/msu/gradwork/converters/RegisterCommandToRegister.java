package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.domain.Register;
import lombok.Synchronized;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;


@Component
public class RegisterCommandToRegister implements Converter<RegisterCommand, Register> {
    @Override
    public Register convert(RegisterCommand registerCommand) {
        return null;
    }

//    private final NoteCommandToNote noteConverter;
//
//    public RegisterCommandToRegister(NoteCommandToNote noteConverter) {
//        this.noteConverter = noteConverter;
//    }
//
//
//    @Synchronized
//    @Nullable
//    @Override
//    public Register convert(RegisterCommand source) {
//
//        if (source == null) {
//            return null;
//        }
//
//        final Register register = new Register();
//        register.setId(source.getId());
//        register.setName(source.getName());
//        register.setNumber(source.getNumber());
//
//        if (source.getNotes() != null && source.getNotes().size() > 0){
//            source.getNotes()
//                    .forEach(note -> register.getNotes().add(noteConverter.convert(note)));
//        }
//
//        return register;
//    }
}
