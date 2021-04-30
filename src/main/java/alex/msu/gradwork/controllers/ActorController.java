package alex.msu.gradwork.controllers;

import alex.msu.gradwork.commands.ActorCommand;
import alex.msu.gradwork.commands.SubjectCommand;
import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.services.ActorService;
import alex.msu.gradwork.services.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class ActorController {

    private final ActorService actorService;
    private final RegisterService registerService;

    public ActorController(ActorService actorService, RegisterService registerService) {
        this.actorService = actorService;
        this.registerService = registerService;
    }

    //Просмотр дел, связанных с данным именным указателем
    @GetMapping("/actors/{actorId}/actorList")
    public String subjectList(@PathVariable String actorId, Model model){

        model.addAttribute("notes", actorService.findAllNoteByActorId(Long.valueOf(actorId)));
        model.addAttribute("actor", actorService.findById(Long.valueOf(actorId)));

        return "/actors/actorList";
    }

    //Сортировка и постраничный просмотр именного указателя
    @GetMapping
    @RequestMapping(value = "/actors/{registerId}/actorListRegister/page/{page-number}")
    public String findPaginated(@PathVariable(name = "registerId") String  registerId,
                                @PathVariable(name = "page-number") final int pageNo,
                                @RequestParam(name = "sort-field") final String sortField,
                                @RequestParam(name = "sort-dir") final String sortDir,
                                final Model model) {
        // Устанавливаем количество записей на странице
        final int pageSize = 100;

        final Page<Actor> page = actorService.findPaginated(Long.valueOf(registerId), pageNo, pageSize, sortField, sortDir);
        final List<Actor> actorList = page.getContent();

        // Параметры постраничного ввода
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        // Параметры сортировки
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        // Список
        model.addAttribute("actorList", actorList);
        // Опись
        model.addAttribute("register", registerService.findById(Long.valueOf(registerId)));
        return "registers/actorListRegister";
    }

    //Редактирования именного указателя
    @GetMapping
    @RequestMapping(value = "/actors/{registerId}/actor/{actorId}/actorUpdate")
    private String subjectUpdate(@PathVariable(name = "registerId") final Long registerId,
                                 @PathVariable(name = "actorId") final Long actorId,
                                 Model model) {

        model.addAttribute("actor", actorService.findById(actorId));
        model.addAttribute("register", registerService.findById(registerId));

        return "/actors/actorUpdate";
    }

    // Внесение изменений в именной указатель
    // Перенаправление на представление именного указателя
    @PostMapping("/actors/{registerId}/actor/{actorId}/actorUpdate")
    public String saveOrUpdate(@ModelAttribute ActorCommand command,
                               @RequestParam(name = "notesText") String notesText,
                               @PathVariable(name = "registerId") final String registerId) {

        ActorCommand savedActorCommand = actorService.saveActorCommand(registerId, notesText, command);
        return "redirect:/actors/" + savedActorCommand.getId() + "/actorList";
    }

    // Удаление именного указателя
    // Перенаправление на спискок именного указателя данной описи
    @GetMapping
    @RequestMapping("/actors/{registerId}/actor/{actorId}/actorDelete")
    public String deleteNote(@PathVariable String registerId,
                             @PathVariable String actorId) {

        actorService.DeleteById(Long.valueOf(registerId), Long.valueOf(actorId));

        return "redirect:/actors/" + registerId + "/actorListRegister/page/1?sort-field=surname&sort-dir=asc";
    }

    // Удаление связи предметного указателя
    @GetMapping
    @RequestMapping("/actors/{registerId}/actor/{actorId}/note/{noteId}/actorDeleteRelationWithNote")
    public String deleteRelationWithNote(@PathVariable String registerId,
                                         @PathVariable String noteId,
                                         @PathVariable String actorId) {

        actorService.DeleteRelationWithNote(Long.valueOf(registerId), Long.valueOf(noteId), Long.valueOf(actorId));

        return "redirect:/actors/" + registerId + "/actor/" + actorId + "/actorUpdate";
    }
}
