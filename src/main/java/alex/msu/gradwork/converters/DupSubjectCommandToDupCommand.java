package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.DupSubjectCommand;
import alex.msu.gradwork.domain.DupSubject;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class DupSubjectCommandToDupCommand implements Converter<DupSubjectCommand, DupSubject> {

    @Synchronized
    @Nullable
    @Override
    public DupSubject convert(DupSubjectCommand source) {

        final DupSubject dupSubject = new DupSubject();
        dupSubject.setId(source.getId());
        dupSubject.setName(source.getName());
        dupSubject.setMemo(source.getMemo());

        return dupSubject;
    }
}
