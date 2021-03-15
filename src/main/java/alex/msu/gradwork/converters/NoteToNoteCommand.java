package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Note;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

public class NoteToNoteCommand implements Converter<Note, NoteCommand> {
    @Override
    public NoteCommand convert(Note note) {
        return null;
    }
//
//    @Synchronized
//    @Nullable
//    @Override
//    public NoteCommand convert(Note source) {
//        if (source==null){
//            return null;
//        }
//
//        final NoteCommand noteCommand = new NoteCommand();
//        noteCommand.setId(source.getId());
//        noteCommand.setActors(source.getActors());
//        noteCommand.setNumber(source.getNumber());
//        noteCommand.setRegister(source.getRegister());
//        noteCommand.setText(source.getText());
//        noteCommand.setNumberOfSheets(source.getNumberOfSheets());
//        return noteCommand;
//    }
}
