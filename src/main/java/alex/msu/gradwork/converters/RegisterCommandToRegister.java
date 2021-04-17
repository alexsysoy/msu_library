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
    private final SubjectCommandToSubject subjectConverter;
    private final ActorCommandToActor actorCommandToActor;

    public RegisterCommandToRegister(NoteCommandToNote noteConverter, SubjectCommandToSubject subjectConverter, ActorCommandToActor actorCommandToActor) {
        this.noteConverter = noteConverter;
        this.subjectConverter = subjectConverter;
        this.actorCommandToActor = actorCommandToActor;
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
        register.setName(command.getName());
        register.setAnnotation(command.getAnnotation());
        register.setMemo(command.getMemo());

        if (command.getNotes() != null && command.getNotes().size()>0){
            command.getNotes()
                    .forEach(note -> register.getNotes().add(noteConverter.convert(note)));
        }

        if (command.getSubjects() != null && command.getSubjects().size()>0){
            command.getSubjects()
                    .forEach(subjectCommand -> register.getSubjects().add(subjectConverter.convert(subjectCommand)));
        }

        if (command.getActors() != null && command.getActors().size() > 0){
            command.getActors()
                    .forEach(actorCommand->register.getActors().add(actorCommandToActor.convert(actorCommand)));
        }



        return register;
    }
}
