package alex.msu.gradwork.controllers;

import alex.msu.gradwork.domain.DupActor;
import alex.msu.gradwork.domain.DupSubject;
import alex.msu.gradwork.services.DupActorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class DupActorController {

    private final DupActorService dupActorService;

    public DupActorController(DupActorService dupActorService) {
        this.dupActorService = dupActorService;
    }

    //Создание дубликата
    @GetMapping("/actors/{actorId}/createDup")
    public String dupCreate(@PathVariable String actorId, Model model){

        DupActor dupActor = dupActorService.createDup(Long.valueOf(actorId));
        log.debug("Создан дубликат с id = {} именного указателя с id = {}", dupActor.getId(), dupActor.getActor().getId());

        return "redirect:/actors/" + dupActor.getActor().getId() + "/actorList";
    }

    //Редактирования дубликата указателя
    @GetMapping
    @RequestMapping(value = "/actors/{dupActorId}/updateDup")
    private String dupUpdate(@PathVariable(name = "dupActorId") final Long dupActorId,
                             Model model) {

        model.addAttribute("dupActor",dupActorService.findById(dupActorId));

        return "actors/dupUpdate";
    }

    // Внесение изменений
    @PostMapping("/actors/{dupActorId}/updateDup/{actorId}")
    public String dupSave(@ModelAttribute DupActor dupActor,
                          @PathVariable(name = "dupActorId") final Long dupActorId,
                          @PathVariable(name = "actorId") final Long actorId) {

        DupActor savedDup = dupActorService.save(dupActor, actorId);

        return "redirect:/actors/" + actorId + "/actorList";
    }




    // Удаление
    @GetMapping
    @RequestMapping("/actors/{dupActorId}/deleteDup/{actorId}")
    public String dupDelete(@PathVariable String dupActorId,
                            @PathVariable String actorId) {

        dupActorService.deleteById(Long.valueOf(dupActorId), Long.valueOf(actorId));

        return "redirect:/actors/" + actorId + "/actorList";
    }
}
