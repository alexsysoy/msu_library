package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.domain.Register;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RegisterToRegisterCommand implements Converter<Register, RegisterCommand> {

    private final NoteToNoteCommand noteConverter;
    private final SubjectToSubjectCommand subjectConverter;
    private final ActorToActorCommand actorToActorCommand;

    public RegisterToRegisterCommand(NoteToNoteCommand noteConverter, SubjectToSubjectCommand subjectConverter, ActorToActorCommand actorToActorCommand) {
        this.noteConverter = noteConverter;
        this.subjectConverter = subjectConverter;
        this.actorToActorCommand = actorToActorCommand;
    }


    @Override
    public RegisterCommand convert(Register source) {

        if (source == null){
            return null;
        }

        final RegisterCommand command = new RegisterCommand();

        command.setId(source.getId());
        command.setName(source.getName());
        command.setAnnotation(source.getAnnotation());
        command.setMemo(source.getMemo());


        if (source.getNotes() != null && source.getNotes().size()>0){
            source.getNotes()
                    .forEach(note -> command.getNotes().add(noteConverter.convert(note)));
        }

        if (source.getSubjects() != null && source.getSubjects().size()>0){
            source.getSubjects()
                    .forEach(subject -> command.getSubjects().add(subjectConverter.convert(subject)));
        }

        if (source.getActors()!=null && source.getActors().size()>0){
            source.getActors()
                    .forEach(actor -> command.getActors().add(actorToActorCommand.convert(actor)));
        }

        return command;
    }
}
