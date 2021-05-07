package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.DupSubjectCommand;
import alex.msu.gradwork.domain.DupSubject;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

public class DupSubjectToDupSubjectCommand implements Converter<DupSubject, DupSubjectCommand> {

    @Synchronized
    @Nullable
    @Override
    public DupSubjectCommand convert(DupSubject source) {

        final DupSubjectCommand dupSubjectCommand = new DupSubjectCommand();

        dupSubjectCommand.setId(source.getId());
        dupSubjectCommand.setName(source.getName());
        dupSubjectCommand.setMemo(source.getMemo());
        return dupSubjectCommand;
    }
}
