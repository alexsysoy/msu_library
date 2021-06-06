package alex.msu.library.services;

import alex.msu.library.commands.NoteCommand;
import alex.msu.library.commands.SubjectCommand;
import alex.msu.library.domain.Subject;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface SubjectService {

    SubjectCommand findByNoteIdAndSubjectId(Long noteId, Long subjectId);

    Set<NoteCommand> findAllNoteBySubjectId (Long l);

    //Возращает постранично отсортированный список предметного указателя по Id Описи
    Page<Subject> findPaginated(Long registerId, int pageNumber, int pageSize, String sortField, String sortDirection);

    SubjectCommand saveSubjectCommand(SubjectCommand command);

    Subject findById(Long l);

    SubjectCommand saveSubjectCommand(String registerId, String notesText, SubjectCommand command);

    void DeleteById(Long registerId, Long idToDelete);

    void DeleteRelationWithNote(Long registerId, Long noteId, Long subjectId);
}
