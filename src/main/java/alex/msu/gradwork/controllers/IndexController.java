package alex.msu.gradwork.controllers;

import alex.msu.gradwork.services.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IndexController {

    private final NoteService noteService;

    public IndexController(NoteService noteService) {
        this.noteService = noteService;
    }

    @RequestMapping({"","/","/index"})
    public String getIndexPage(Model model){
        log.debug("Getting Index page! Debug");
        model.addAttribute("notes", noteService.getNotes());
        return "index";
    }
}
