package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.domain.Note;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.repositories.NoteRepository;
import alex.msu.gradwork.repositories.RegisterRepository;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchService {

    private final RegisterRepository registerRepository;
    private final NoteRepository noteRepository;

    public SearchService(RegisterRepository registerRepository, NoteRepository noteRepository) {
        this.registerRepository = registerRepository;
        this.noteRepository = noteRepository;
    }


    public Set<Note> searchNotes(String wordToSearch) {

        Set<Note> notes = new HashSet<>();

        for (Register register : registerRepository.findAll()) {

            Set<Note> registerNotes = noteRepository.findNotesByRegisterId(register.getId());
            if (registerNotes.size() > 0) {
                for (Note note : registerNotes){

                    if (note.getMemo() != null && note.getMemo().contains(wordToSearch)) {
                        notes.add(note);
                    }

                    if (note.getAnnotation() != null && note.getAnnotation().contains(wordToSearch)) {
                        notes.add(note);
                    }

                    if (note.getActors().size()>0) {
                        for (Actor actor : note.getActors()){
                            if (actor.getName().contains(wordToSearch) || actor.getSurname().contains(wordToSearch) || actor.getPatronymic().contains(wordToSearch)){
                                notes.add(note);
                            }
                        }
                    }

                    if (note.getSubjects().size()>0) {
                        for (Subject subject : note.getSubjects()){
                            if (subject.getName().contains(wordToSearch)){
                                notes.add(note);
                            }
                            if (subject.getDupSubject() != null) {
                                if (subject.getDupSubject().getName().strip().toLowerCase().contains(wordToSearch)) notes.add(note);
                            }
                        }
                    }

                }
            }

        }

        return notes;
    }

//    // Расширенный поиск единиц хранения по данной описи
//    @Transactional
//    @Synchronized
//    public Set<Note> searchNotes(NoteCommand command) {
//
//        Set<Note> notes = noteRepository.findNotesByRegisterId(command.getRegisterId());
//
//        log.debug("Общее количество Дел в Описи {}", notes.size());
//
//        // Ищем по номеру
//        if (command.getNumber() != null) {
//            notes = notes.stream().filter(note -> note.getNumber().equals(command.getNumber())).collect(Collectors.toSet());
//            log.debug("Количество дел {} после поиска по номеру", notes.size());
//        }
//
//
//        // Ищем по аннотоции
//        if (!command.getAnnotation().isEmpty() && notes.size() > 0) {
//            notes = notes.stream().filter(note -> note.getAnnotation().toLowerCase().contains(command.getAnnotation().toLowerCase())).collect(Collectors.toSet());
//            log.debug("Количество дел {} после поиска по аннотации", notes.size());
//        }
//
//
//        // Ищем по примечанию
//        if (!command.getMemo().isEmpty() && notes.size() > 0) {
//            notes = notes.stream()
//                    .filter(note -> !(note.getMemo() == null))
//                    .filter(note -> note.getMemo().toLowerCase().contains(command.getMemo().toLowerCase())).collect(Collectors.toSet());
//            log.debug("Количество дел {} после поиска по примечанию" , notes.size());
//        }
//
//
//        // Ищем по Предметному указателю
//        if (!command.getFindSubject().isEmpty() && notes.size() > 0) {
//            notes = notes.stream()
//                    .filter(note -> note.getSubjects().stream()
//                            .anyMatch(subject -> subject.getName().toLowerCase().contains(command.getFindSubject().toLowerCase().trim())))
//                    .collect(Collectors.toSet());
//            log.debug("Количество дел {} после поиска по Предметному указателю", notes.size());
//        }
//
//
//        // Ищем по Именному указателю
//        if (!command.getFindNameActor().isEmpty() || !command.getFindPatronymicActor().isEmpty() || !command.getFindSurnameActor().isEmpty() && notes.size() > 0) {
//            notes = notes.stream()
//                    .filter(note -> note.getActors().stream()
//                            .anyMatch(actor -> actor.getName().toLowerCase().contains(command.getFindNameActor().toLowerCase().trim())))
//                    .filter(note -> note.getActors().stream()
//                            .anyMatch(actor -> actor.getPatronymic().toLowerCase().contains(command.getFindPatronymicActor().toLowerCase().trim())))
//                    .filter(note -> note.getActors().stream()
//                            .anyMatch(actor -> actor.getSurname().toLowerCase().contains(command.getFindSurnameActor().toLowerCase().trim())))
//                    .collect(Collectors.toSet());
//            log.debug("Количество дел {} после поиска по именному указателю", notes.size());
//        }
//
//        return notes;
//    }
}
