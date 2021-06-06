package alex.msu.library.converters;

import alex.msu.library.commands.SubjectCommand;
import alex.msu.library.domain.Subject;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;



@Component
public class SubjectCommandToSubject implements Converter<SubjectCommand, Subject> {

    @Synchronized
    @Nullable
    @Override
    public Subject convert(SubjectCommand source) {
        if (source == null) {
            return null;
        }

        final Subject subject = new Subject();
        subject.setId(source.getId());
        subject.setName(source.getName());
        subject.setMemo(source.getMemo());

        return subject;
    }
}
