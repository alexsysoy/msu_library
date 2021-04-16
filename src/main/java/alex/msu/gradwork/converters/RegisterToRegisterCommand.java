package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.domain.Register;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RegisterToRegisterCommand implements Converter<Register, RegisterCommand> {

    private final NoteToNoteCommand noteConverter;
    private final SubjectToSubjectCommand subjectConverter;

    public RegisterToRegisterCommand(NoteToNoteCommand noteConverter, SubjectToSubjectCommand subjectConverter) {
        this.noteConverter = noteConverter;
        this.subjectConverter = subjectConverter;
    }


    @Override
    public RegisterCommand convert(Register source) {

        if (source == null){
            return null;
        }

        final RegisterCommand command = new RegisterCommand();
        command.setName(source.getName());
        command.setId(source.getId());
        command.setNumber(source.getNumber());



        if (source.getNotes() != null && source.getNotes().size()>0){
            source.getNotes()
                    .forEach(note -> command.getNotes().add(noteConverter.convert(note)));
        }

        if (source.getSubjects() != null && source.getSubjects().size()>0){
            source.getSubjects()
                    .forEach(subject -> command.getSubjects().add(subjectConverter.convert(subject)));
        }

        return command;
    }
}
