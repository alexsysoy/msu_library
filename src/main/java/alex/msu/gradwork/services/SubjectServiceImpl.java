package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.commands.SubjectCommand;
import alex.msu.gradwork.converters.NoteToNoteCommand;
import alex.msu.gradwork.converters.SubjectCommandToSubject;
import alex.msu.gradwork.converters.SubjectToSubjectCommand;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import alex.msu.gradwork.repositories.SubjectRepository;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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

    //Возращает постранично отсортированный список предметного указателя по Id Описи
    @Override
    public Page<Subject> findPaginated(Long registerId, int pageNumber, int pageSize, String sortField, String sortDirection) {

        final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return subjectRepository.findSubjectsByRegisterId(registerId, pageable);
    }


    @Override
    public SubjectCommand saveSubjectCommand(SubjectCommand command) {
        return new SubjectCommand();
    }

    @Override
    public Subject findById(Long l){
        return subjectRepository.findById(l).get();
    }

    @Override
    @Transactional
    @Synchronized
    public SubjectCommand saveSubjectCommand(String registerId, String notesText, SubjectCommand command) {

        Optional<Register> registerOptional = registerRepository.findById(Long.valueOf(registerId));
        Register registerFound = registerOptional.get();
        Optional<Subject> subjectOptional = subjectRepository.findById(command.getId());
        Subject subjectFound = subjectOptional.get();

        subjectFound.setName(command.getName());
        subjectFound.setMemo(command.getMemo());

        //Парсим номера дел, которые надо прикрепить к предметному указателю
        if (!notesText.equals("")) {
            String[] parts = notesText.split("_");
            Set<Long> noteNumbers = new HashSet<>();

            for (String string : parts){
                try {
                    noteNumbers.add(Long.valueOf(string));
                } catch (NumberFormatException e) {
                    continue;
                }
            }

            for (Long l : noteNumbers) {
                //Добавляем предметный указатель к делу, если дело с данным номером существует
                Optional<Note> noteOptional = noteRepository.findNoteByNumber(l);
                noteOptional.ifPresent(note -> note.getSubjects().add(subjectFound));
            }
        }

        return subjectToSubjectCommand.convert(subjectFound);
    }

    @Override
    public void DeleteById(Long registerId, Long idToDelete) {
        log.debug("Удаляем предметный указатель id {} и id Описи {}", idToDelete, registerId);

        //Ищем Опись с данным id
        Optional<Register> registerOptional = registerRepository.findById(registerId);
        if(registerOptional.isPresent()){
            Register register = registerOptional.get();

            Optional<Subject> subjectOptional = register.getSubjects().stream()
                    .filter(subject -> subject.getId().equals(idToDelete))
                    .findFirst();

            if(subjectOptional.isPresent()){

                Subject subjectToDelete = subjectOptional.get();
                //Уничтожаем ссылки предмета из описи
                subjectToDelete.setRegister(null);
                register.getSubjects().remove(subjectToDelete);
                //Уничтожаем ссылки предмета из дел
                for (Note note: subjectToDelete.getNotes()){
                    note.getSubjects().remove(subjectToDelete);
                }
                subjectToDelete.setNotes(null);
                //Удаляем Дело
                subjectRepository.deleteById(idToDelete);
                //Сохраняем новое состояние Описи
                registerRepository.save(register);
            }
        } else {
            log.debug("Опись с id {} не найдена:", registerId);
        }

    }

    @Override
    public void DeleteRelationWithNote(Long registerId, Long noteId, Long subjectId) {

        //Ищем Опись с данным id
        Optional<Register> registerOptional = registerRepository.findById(registerId);
        if(registerOptional.isPresent()){
            Register register = registerOptional.get();

            //Ищем в описи предмет и дело
            Optional<Subject> subjectOptional = register.getSubjects().stream()
                    .filter(subject -> subject.getId().equals(subjectId))
                    .findFirst();

            Optional<Note> noteOptional = register.getNotes().stream()
                    .filter(note -> note.getId().equals(noteId))
                    .findFirst();


            if(subjectOptional.isPresent() && noteOptional.isPresent()){

                Subject subject = subjectOptional.get();
                Note note = noteOptional.get();

                //Уничтожаем ссылки
                subject.getNotes().remove(note);
                note.getSubjects().remove(subject);

                //Сохраняем новое состояние Описи
                registerRepository.save(register);
            }
        } else {
            log.debug("Опись с id {} не найдена:", registerId);
        }
    }
}
