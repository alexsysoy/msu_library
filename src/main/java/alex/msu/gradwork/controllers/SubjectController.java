package alex.msu.gradwork.controllers;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.commands.SubjectCommand;
import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.services.RegisterService;
import alex.msu.gradwork.services.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
public class SubjectController {

    private final SubjectService subjectService;
    private final RegisterService registerService;

    public SubjectController(SubjectService subjectService, RegisterService registerService) {
        this.subjectService = subjectService;
        this.registerService = registerService;
    }

    //Просмотр дел, связанных с данным предметным указателем
    @GetMapping("/subjects/{subjectId}/subjectList")
    public String subjectList(@PathVariable String subjectId, Model model){

        model.addAttribute("notes", subjectService.findAllNoteBySubjectId(Long.valueOf(subjectId)));
        model.addAttribute("subject", subjectService.findById(Long.valueOf(subjectId)));

        return "subjects/subjectList";
    }

    //Сортировка и постраничный просмотр предметного указателя
    @GetMapping
    @RequestMapping(value = "/subjects/{registerId}/subjectListRegister/page/{page-number}")
    public String findPaginated(@PathVariable(name = "registerId") String  registerId,
                                @PathVariable(name = "page-number") final int pageNo,
                                @RequestParam(name = "sort-field") final String sortField,
                                @RequestParam(name = "sort-dir") final String sortDir,
                                final Model model) {
        // Устанавливаем количество записей на странице
        final int pageSize = 100;

        final Page<Subject> page = subjectService.findPaginated(Long.valueOf(registerId), pageNo, pageSize, sortField, sortDir);
        final List<Subject> subjectList = page.getContent();

        // Параметры постраничного ввода
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        // Параметры сортировки
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        // Список
        model.addAttribute("subjectList", subjectList);
        // Опись
        model.addAttribute("register", registerService.findById(Long.valueOf(registerId)));
        return "registers/subjectListRegister";
    }

    //Редактирования предметного указателя
    @GetMapping
    @RequestMapping(value = "/subjects/{registerId}/subject/{subjectId}/subjectUpdate")
    private String subjectUpdate(@PathVariable(name = "registerId") final Long registerId,
                                 @PathVariable(name = "subjectId") final Long subjectId,
                                 Model model) {

        model.addAttribute("subject", subjectService.findById(subjectId));
        model.addAttribute("register", registerService.findById(registerId));

        return "subjects/subjectUpdate";
    }

    // Внесение изменений в предметный указатель
    // Перенаправление на представление данного указателя
    @PostMapping("/subjects/{registerId}/subject/{subjectId}/subjectUpdate")
    public String saveOrUpdate(@ModelAttribute SubjectCommand command,
                               @RequestParam(name = "notesText") String notesText,
                               @PathVariable(name = "registerId") final String registerId
                               ){
        SubjectCommand savedSubjectCommand = subjectService.saveSubjectCommand(registerId, notesText, command);
        return "redirect:/subjects/" + savedSubjectCommand.getId() + "/subjectList";
    }

    // Удаление предметного указателя
    // Перенаправление на спискок предметного указателя данной описи
    @GetMapping
    @RequestMapping("/subjects/{registerId}/subject/{subjectId}/subjectDelete")
    public String deleteNote(@PathVariable String registerId,
                             @PathVariable String subjectId) {

        subjectService.DeleteById(Long.valueOf(registerId), Long.valueOf(subjectId));

        return "redirect:/subjects/" + registerId + "/subjectListRegister/page/1?sort-field=name&sort-dir=asc";
    }

    // Удаление связи предметного указателя
    @GetMapping
    @RequestMapping("/subjects/{registerId}/subject/{subjectId}/note/{noteId}/subjectDeleteRelationWithNote")
    public String deleteRelationWithNote(@PathVariable String registerId,
                                         @PathVariable String noteId,
                                         @PathVariable String subjectId) {

        subjectService.DeleteRelationWithNote(Long.valueOf(registerId), Long.valueOf(noteId), Long.valueOf(subjectId));

        return "redirect:/subjects/" + registerId + "/subject/" + subjectId + "/subjectUpdate";
    }

}
