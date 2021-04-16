package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.SubjectCommand;
import alex.msu.gradwork.converters.SubjectCommandToSubject;
import alex.msu.gradwork.converters.SubjectToSubjectCommand;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class SubjectServiceImpl implements SubjectService {

    private final RegisterRepository registerRepository;
    private final NoteRepository noteRepository;
    private final SubjectToSubjectCommand subjectToSubjectCommand;
    private final SubjectCommandToSubject subjectCommandToSubject;

    public SubjectServiceImpl(RegisterRepository registerRepository, NoteRepository noteRepository, SubjectToSubjectCommand subjectToSubjectCommand, SubjectCommandToSubject subjectCommandToSubject) {
        this.registerRepository = registerRepository;
        this.noteRepository = noteRepository;
        this.subjectToSubjectCommand = subjectToSubjectCommand;
        this.subjectCommandToSubject = subjectCommandToSubject;
    }

    @Override
    public SubjectCommand findByNoteIdAndSubjectId(Long noteId, Long subjectId) {

//        Optional<Register> optionalRegister = registerRepository.findById(registerId);
//        //todo impl error handling if optionalRegister is not present
//        Register register = optionalRegister.get();

        Optional<Note> optionalNote = noteRepository.findById(noteId);
        Note note = optionalNote.get();

        Optional<SubjectCommand> subjectCommandOptional = note.getSubjects().stream()
                .filter(subject -> subject.getId().equals(subjectId))
                .map(subjectToSubjectCommand::convert).findFirst();

        return subjectCommandOptional.get();
    }

    @Override
    public SubjectCommand saveSubjectCommand(SubjectCommand command) {
        return new SubjectCommand();
    }
}
