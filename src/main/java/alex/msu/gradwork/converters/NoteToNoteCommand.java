package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Note;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NoteToNoteCommand implements Converter<Note, NoteCommand> {

    @Synchronized
    @Nullable
    @Override
    public NoteCommand convert(Note note) {
        if (note==null){
            return null;
        }

        final NoteCommand noteCommand = new NoteCommand();
        noteCommand.setId(note.getId());
        noteCommand.setActors(note.getActors());
        noteCommand.setNumber(note.getNumber());
        if (note.getRegister() != null) {
            noteCommand.setRegisterId(note.getRegister().getId());
        }
        noteCommand.setText(note.getText());
        noteCommand.setNumberOfSheets(note.getNumberOfSheets());
        return noteCommand;
    }
}
