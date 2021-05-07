package alex.msu.gradwork.controllers;

import alex.msu.gradwork.domain.DupSubject;
import alex.msu.gradwork.services.DupSubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class DupSubjectController {

    private final DupSubjectService dupSubjectService;

    public DupSubjectController(DupSubjectService dupSubjectService) {
        this.dupSubjectService = dupSubjectService;
    }


    //Создание дубликата
    @GetMapping("/subjects/{subjectId}/createDup")
    public String dupCreate(@PathVariable String subjectId, Model model){

        DupSubject dupSubject = dupSubjectService.createDup(Long.valueOf(subjectId));
        log.debug("Создан дубликат с id = {} предметного указателя с id = {}", dupSubject.getId(), dupSubject.getSubject().getId());

        return "redirect:/subjects/" + dupSubject.getSubject().getId() + "/subjectList";
    }

    //Редактирования дубликата указателя
    @GetMapping
    @RequestMapping(value = "/subjects/{dupSubjectId}/updateDup")
    private String dupUpdate(@PathVariable(name = "dupSubjectId") final Long dupSubjectId,
                                 Model model) {

        model.addAttribute("dupSubject",dupSubjectService.findById(dupSubjectId));

        return "subjects/dupUpdate";
    }

    // Внесение изменений
    @PostMapping("/subjects/{dupSubjectId}/updateDup/{subjectId}")
    public String dupSave(@ModelAttribute DupSubject dupSubject,
                          @PathVariable(name = "dupSubjectId") final Long dupSubjectId,
                          @PathVariable(name = "subjectId") final Long subjectId) {

        DupSubject savedDup = dupSubjectService.save(dupSubject, subjectId);

        return "redirect:/subjects/" + subjectId + "/subjectList";
    }


    // Удаление
    @GetMapping
    @RequestMapping("/subjects/{dupSubjectId}/deleteDup/{subjectId}")
    public String dupDelete(@PathVariable String dupSubjectId,
                            @PathVariable String subjectId) {

        dupSubjectService.deleteById(Long.valueOf(dupSubjectId), Long.valueOf(subjectId));

        return "redirect:/subjects/" + subjectId + "/subjectList";
    }
}


