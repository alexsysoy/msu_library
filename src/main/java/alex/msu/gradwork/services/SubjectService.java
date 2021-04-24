package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.commands.SubjectCommand;

import java.util.Set;

public interface SubjectService {

    SubjectCommand findByNoteIdAndSubjectId(Long noteId, Long subjectId);

    Set<NoteCommand> findAllNoteBySubjectId (Long l);

    SubjectCommand saveSubjectCommand(SubjectCommand command);

}
