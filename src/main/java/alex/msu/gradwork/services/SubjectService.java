package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.commands.SubjectCommand;
import alex.msu.gradwork.domain.Subject;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface SubjectService {

    SubjectCommand findByNoteIdAndSubjectId(Long noteId, Long subjectId);

    Set<NoteCommand> findAllNoteBySubjectId (Long l);

    //Возращает постранично отсортированный список предметного указателя по Id Описи
    Page<Subject> findPaginated(Long registerId, int pageNumber, int pageSize, String sortField, String sortDirection);

    SubjectCommand saveSubjectCommand(SubjectCommand command);

}
