package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.domain.Register;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RegisterToRegisterCommand implements Converter<Register, RegisterCommand> {
    @Override
    public RegisterCommand convert(Register register) {
        return null;
    }

//    @Synchronized
//    @Nullable
//    @Override
//    public RegisterCommand convert(Register source) {
//        if (source == null) {
//            return null;
//        }
//
//        final RegisterCommand registerCommand = new RegisterCommand();
//        registerCommand.setId(source.getId());
//        registerCommand.setName(source.getName());
//        registerCommand.setNumber(source.getNumber());
//        registerCommand.setNotes(source.getNotes());
//        return registerCommand;
//    }




}
