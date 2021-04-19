package alex.msu.gradwork.controllers;

import alex.msu.gradwork.services.ActorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    //Просмотр дел, связанных с данным именным указателем
    @GetMapping("/actors/{actorId}/actorList")
    public String subjectList(@PathVariable String actorId, Model model){

        model.addAttribute("notes", actorService.findAllNoteByActorId(Long.valueOf(actorId)));

        return "/actors/actorList";
    }

}
