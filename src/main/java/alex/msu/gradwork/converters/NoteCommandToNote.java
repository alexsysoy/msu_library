package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;



@Component
public class NoteCommandToNote implements Converter<NoteCommand, Note> {
    @Override
    public Note convert(NoteCommand noteCommand) {
        return null;
    }
//
//    @Nullable
//    @Override
//    public Note convert(NoteCommand source){
//        if (source == null) {
//            return null;
//        }
//
//        final Note note = new Note();
//        note.setId(source.getId());
//        note.setNumber(source.getNumber());
//        note.setText(source.getText());
//        note.setNumberOfSheets(source.getNumberOfSheets());
//
//        if (source.getRegisterId() != null) {
//            Register register = new Register();
//            register.setId(source.getRegisterId());
//            note.setRegister(register);
//        }
//
//        note.setRegister(source.getRegister());
//        note.setActors(source.getActors());
//        return note;
//
//    }

}
