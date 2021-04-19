package alex.msu.gradwork.controllers;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.services.NoteService;
import alex.msu.gradwork.services.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@Slf4j
@Controller
public class SubjectController {

    private final NoteService noteService;
    private final SubjectService subjectService;

    public SubjectController(NoteService noteService, SubjectService subjectService) {
        this.noteService = noteService;
        this.subjectService = subjectService;
    }

    //Просмотр дел, связанных с данным предметным указателем
    @GetMapping("/subjects/{subjectId}/subjectList")
    public String subjectList(@PathVariable String subjectId, Model model){

        model.addAttribute("notes", subjectService.findAllNoteBySubjectId(Long.valueOf(subjectId)));

        return "/subjects/subjectList";
    }



    @GetMapping("/note/{noteId}/subjects")
    public String listSubjects(@PathVariable String noteId, Model model){

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
