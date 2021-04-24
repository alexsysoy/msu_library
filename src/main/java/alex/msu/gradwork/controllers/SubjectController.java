package alex.msu.gradwork.controllers;

import alex.msu.gradwork.services.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    //Просмотр дел, связанных с данным предметным указателем
    @GetMapping("/subjects/{subjectId}/subjectList")
    public String subjectList(@PathVariable String subjectId, Model model){

        model.addAttribute("notes", subjectService.findAllNoteBySubjectId(Long.valueOf(subjectId)));

        return "/subjects/subjectList";
    }

}
