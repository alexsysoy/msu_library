package alex.msu.gradwork.controllers;

import alex.msu.gradwork.domain.Subject;
import alex.msu.gradwork.services.RegisterService;
import alex.msu.gradwork.services.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        return "/subjects/subjectList";
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

}
