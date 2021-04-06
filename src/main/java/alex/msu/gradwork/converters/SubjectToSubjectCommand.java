package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.SubjectCommand;
import alex.msu.gradwork.domain.Subject;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class SubjectToSubjectCommand implements Converter<Subject, SubjectCommand> {

    @Synchronized
    @Nullable
    @Override
    public SubjectCommand convert(Subject source) {

        if (source == null){
            return null;
        }

        final SubjectCommand subjectCommand = new SubjectCommand();

        subjectCommand.setId(source.getId());
        subjectCommand.setName(source.getName());
        subjectCommand.setMemo(source.getMemo());
        return subjectCommand;
    }
}
