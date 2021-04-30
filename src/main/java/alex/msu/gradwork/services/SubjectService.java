package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.commands.SubjectCommand;
import alex.msu.gradwork.domain.Subject;
import lombok.Synchronized;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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
