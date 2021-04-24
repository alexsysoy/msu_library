package alex.msu.gradwork.controllers;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.domain.Register;
import alex.msu.gradwork.services.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    // URL - http://localhost:8080/
    //Направляем на сортировку и на постраничный просмотр
    @GetMapping(value = "/")
    public String viewIndexPage() {
        return "redirect:page/1?sort-field=id&sort-dir=asc";
    }

    // URL - http://localhost:8080/page/1?sort-field=firstName&sort-dir=desc
    @GetMapping(value = "/page/{page-number}")
    public String findPaginated(@PathVariable(name = "page-number") final int pageNo,
                                @RequestParam(name = "sort-field") final String sortField,
                                @RequestParam(name = "sort-dir") final String sortDir,
                                final Model model) {
        // Устанавливаем количество записей на странице
        final int pageSize = 15;
        final Page<Register> page = registerService.findPaginated(pageNo, pageSize, sortField, sortDir);
        final List<Register> registerList = page.getContent();

        // Параметры постраничного ввода
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        // Параметры сортировки
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        // список Описей
        model.addAttribute("registerList", registerList);
        return "main";
    }

    // Направляем на Предметный указатель Описи
    @GetMapping(value = "/registers/{RegisterId}/subjectListRegister")
    public String viewSubjectRegister(@PathVariable(name = "RegisterId") final Long RegisterId,
                                      Model model){
        model.addAttribute("register", registerService.findById(RegisterId));
        return "/registers/subjectListRegister";
    }

    // Направляем на Именной указатель Описи
    @GetMapping(value = "/registers/{RegisterId}/actorListRegister")
    public String viewActorRegister(@PathVariable(name = "RegisterId") final Long RegisterId,
                                      Model model){
        model.addAttribute("register", registerService.findById(RegisterId));
        return "/registers/actorListRegister";
    }

    // Направляем на просмотр данный Описи
    @GetMapping(value = "/registers/{registerId}/registerShowGrid")
    public String viewRegisterShowGrid(@PathVariable String registerId, Model model) {

        model.addAttribute("register", registerService.findById(Long.valueOf(registerId)));
        return "/registers/registerShowGrid";
    }

    // Направляем на создание новой Описи
    @GetMapping(value = "/registers/registerCreate")
    public String createRegister(Model model) {

        RegisterCommand registerCommand = new RegisterCommand();

        model.addAttribute("register", registerCommand);

        return "/registers/registerCreate";
    }

    // Принимаем данные на создание новой Описи
    @PostMapping(value = "/registers/registerCreate")
    public String createRegister(@ModelAttribute RegisterCommand command) {

        registerService.saveRegisterCommand(command);

        return "redirect:/";
    }

    // Удаление Описи
    @GetMapping
    @RequestMapping("/registers/{registerId}/registerDelete")
    public String deleteNote(@PathVariable String registerId){

        registerService.deleteById(Long.valueOf(registerId));

        return "redirect:/";
    }

    // Редактирование Описи
    @GetMapping
    @RequestMapping("/registers/{registerId}/registerUpdate")
    public String updateRecipeNote(@PathVariable String registerId, Model model) {

        model.addAttribute("register", registerService.findById(Long.valueOf(registerId)));
        return "/registers/registerUpdate";
    }

    // Внесение изменений в Опись
    @PostMapping("/registers/registerUpdate")
    public String saveOrUpdate(@ModelAttribute RegisterCommand command){

        registerService.saveRegisterCommand(command);

        return "redirect:/";
    }

}
