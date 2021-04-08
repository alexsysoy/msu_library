package alex.msu.gradwork.controllers;

import alex.msu.gradwork.services.NoteService;
import alex.msu.gradwork.services.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class SubjectController {

    private final NoteService noteService;
    private final SubjectService subjectService;

    public SubjectController(NoteService noteService, SubjectService subjectService) {
        this.noteService = noteService;
        this.subjectService = subjectService;
    }

    @GetMapping("/note/{noteId}/subjects")
    public String listSubjects(@PathVariable String noteId, Model model){
        log.debug("Getting subject list for note id: " + noteId);

        // use command object to avoid lazy load errors in Thymeleaf.
        model.addAttribute("note", noteService.findById(Long.valueOf(noteId)));

        return "register/note/subject/list";
    }


    @GetMapping("/note/{noteId}/subject/{subjectId}/show")
    public String showNoteSubject(@PathVariable String noteId,
                                  @PathVariable String subjectId, Model model){

        model.addAttribute("subject",subjectService.findByNoteIdAndSubjectId(Long.valueOf(noteId), Long.valueOf(subjectId)));
        return "register/note/subject/show";
    }
}
