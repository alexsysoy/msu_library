package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;



@Component
public class NoteCommandToNote implements Converter<NoteCommand, Note> {

    @Nullable
    @Override
    public Note convert(NoteCommand command){
        if (command == null) {
            return null;
        }

        final Note note = new Note();
        note.setId(command.getId());
        note.setNumber(command.getNumber());
        note.setText(command.getText());
        note.setNumberOfSheets(command.getNumberOfSheets());

        if (command.getRegisterId() != null) {
            Register register = new Register();
            register.setId(command.getRegisterId());
            note.setRegister(register);
            register.addNote(note);
        }

        return note;
    }

}
