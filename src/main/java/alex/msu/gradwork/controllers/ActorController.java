package alex.msu.gradwork.controllers;

import alex.msu.gradwork.domain.Actor;
import alex.msu.gradwork.services.ActorService;
import alex.msu.gradwork.services.RegisterService;
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

}
