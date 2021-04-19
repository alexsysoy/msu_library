package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.commands.SubjectCommand;
import alex.msu.gradwork.converters.NoteToNoteCommand;
import alex.msu.gradwork.converters.SubjectCommandToSubject;
import alex.msu.gradwork.converters.SubjectToSubjectCommand;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import alex.msu.gradwork.repositories.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubjectServiceImpl implements SubjectService {

    private final RegisterRepository registerRepository;
    private final SubjectRepository subjectRepository;
    private final NoteRepository noteRepository;
    private final SubjectToSubjectCommand subjectToSubjectCommand;
    private final SubjectCommandToSubject subjectCommandToSubject;
    private final NoteToNoteCommand noteToNoteCommand;

    public SubjectServiceImpl(RegisterRepository registerRepository, SubjectRepository subjectRepository, NoteRepository noteRepository, SubjectToSubjectCommand subjectToSubjectCommand, SubjectCommandToSubject subjectCommandToSubject, NoteToNoteCommand noteToNoteCommand) {
        this.registerRepository = registerRepository;
        this.subjectRepository = subjectRepository;
        this.noteRepository = noteRepository;
        this.subjectToSubjectCommand = subjectToSubjectCommand;
        this.subjectCommandToSubject = subjectCommandToSubject;
        this.noteToNoteCommand = noteToNoteCommand;
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

    //Возвращаем множество Дел, принадлежащих данному предметному указателю
    @Override
    public Set<NoteCommand> findAllNoteBySubjectId(Long subjectId) {

        Set<NoteCommand> notes = new HashSet<>();
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);

        if (optionalSubject.isPresent()){
            notes = optionalSubject.get().getNotes().stream().map(noteToNoteCommand::convert).collect(Collectors.toSet());
        }

        return notes;
    }


    @Override
    public SubjectCommand saveSubjectCommand(SubjectCommand command) {
        return new SubjectCommand();
    }
}
