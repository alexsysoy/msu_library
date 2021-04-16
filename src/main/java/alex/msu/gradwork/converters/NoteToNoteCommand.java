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

    public NoteToNoteCommand(SubjectToSubjectCommand subjectToSubjectCommand) {
        this.subjectToSubjectCommand = subjectToSubjectCommand;
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
        //noteCommand.setActors(source.getActors());
        noteCommand.setNumber(note.getNumber());
        noteCommand.setMemo(note.getMemo());
        noteCommand.setAnnotation(note.getAnnotation());

        if (note.getSubjects() != null && note.getSubjects().size()>0){
            note.getSubjects()
                    .forEach((Subject subject) -> noteCommand.getSubjects().add(subjectToSubjectCommand.convert(subject)));
        }

        if (note.getRegister() != null) {
            noteCommand.setRegisterId(note.getRegister().getId());
        }

        return noteCommand;
    }
}
