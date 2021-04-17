package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Image;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;



@Component
public class NoteCommandToNote implements Converter<NoteCommand, Note> {

    private final SubjectCommandToSubject subjectCommandToSubject;
    private final ActorCommandToActor actorCommandToActor;

    public NoteCommandToNote(SubjectCommandToSubject subjectCommandToSubject, ActorCommandToActor actorCommandToActor) {
        this.subjectCommandToSubject = subjectCommandToSubject;
        this.actorCommandToActor = actorCommandToActor;
    }

    @Nullable
    @Override
    public Note convert(NoteCommand command){
        if (command == null) {
            return null;
        }

        final Note note = new Note();

        note.setId(command.getId());
        note.setNumber(command.getNumber());
        note.setAnnotation(command.getAnnotation());
        note.setMemo(command.getMemo());


        if (command.getRegisterId() != null) {
            Register register = new Register();
            register.setId(command.getRegisterId());
            note.setRegister(register);
            register.addNote(note);
        }

        if (command.getImageId() != 0) {
            Image image = new Image();
            image.setId(command.getImageId());
            note.setImage(image);
            image.addNote(note);
        }

        if (command.getSubjects() != null && command.getSubjects().size() > 0) {
            command.getSubjects().forEach(subject -> note.getSubjects().add(subjectCommandToSubject.convert(subject)));
        }

        if (command.getActors() != null && command.getActors().size() > 0 ){
            command.getActors().forEach(actorCommand -> note.getActors().add(actorCommandToActor.convert(actorCommand)));
        }


        return note;
    }

}
