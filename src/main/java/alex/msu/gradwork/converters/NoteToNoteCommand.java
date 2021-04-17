package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Subject;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NoteToNoteCommand implements Converter<Note, NoteCommand> {

    private final SubjectToSubjectCommand subjectToSubjectCommand;
    private final ActorToActorCommand actorToActorCommand;

    public NoteToNoteCommand(SubjectToSubjectCommand subjectToSubjectCommand, ActorToActorCommand actorToActorCommand) {
        this.subjectToSubjectCommand = subjectToSubjectCommand;
        this.actorToActorCommand = actorToActorCommand;
    }

    @Synchronized
    @Nullable
    @Override
    public NoteCommand convert(Note note) {
        if (note==null){
            return null;
        }

        final NoteCommand noteCommand = new NoteCommand();

        noteCommand.setId(note.getId());
        noteCommand.setNumber(note.getNumber());
        noteCommand.setMemo(note.getMemo());
        noteCommand.setAnnotation(note.getAnnotation());

        if (note.getRegister() != null) {
            noteCommand.setRegisterId(note.getRegister().getId());
        }

        if (note.getImage() != null) {
            noteCommand.setImageId(note.getImage().getId());
        }

        if (note.getSubjects() != null && note.getSubjects().size()>0){
            note.getSubjects()
                    .forEach((Subject subject) -> noteCommand.getSubjects().add(subjectToSubjectCommand.convert(subject)));
        }

        if (note.getActors() != null && note.getActors().size() > 0){
            note.getActors()
                    .forEach(actor -> noteCommand.getActors().add(actorToActorCommand.convert(actor)));
        }

        return noteCommand;
    }
}
